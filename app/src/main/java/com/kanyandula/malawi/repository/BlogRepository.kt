package com.kanyandula.malawi.repository


import com.kanyandula.malawi.api.BlogApi
import android.content.ContentValues.TAG
import android.util.Log
import androidx.room.withTransaction

import com.bumptech.glide.load.HttpException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.kanyandula.malawi.api.BlogDtoMapper
import com.kanyandula.malawi.api.BlogResponse
import com.kanyandula.malawi.data.model.Blog
import com.kanyandula.malawi.data.BlogDataBase
import com.kanyandula.malawi.data.model.BlogEntityMapper
import com.kanyandula.malawi.data.model.LatestBlogs
import com.kanyandula.malawi.utils.Resource
import com.kanyandula.malawi.utils.networkBoundResource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.tasks.await
import java.io.IOException
import java.lang.Exception
import java.util.concurrent.TimeUnit
import javax.inject.Inject



class BlogRepository @Inject constructor(
    private  val blogApi: BlogApi,
    private var blogRef: DatabaseReference,
    private val   blogDataBase: BlogDataBase,
    private val entityMapper: BlogEntityMapper,
    private val blogDtoMapper: BlogDtoMapper,
    private val databaseAuth: FirebaseAuth
){

     private val blogDao = blogDataBase.blogDao()

    fun getFeed(
        forceRefresh: Boolean,
        onFetchSuccess: () -> Unit,
        onFetchFailed: (Throwable) -> Unit
    )= networkBoundResource(
        query = {
            blogDao.getAllBlogFeed()
        },
        fetch = {
            fetchBlogPost().blogs


        },
        saveFetchResult = {  serverBlogArticles ->

            val bookmarkedArticles = blogDao.getAllBookmarkedBlogs().first()

            val newBlogs =
                serverBlogArticles?.map { serverBlogArticle ->

                    val isBookmarked = bookmarkedArticles.any { bookmarkedArticle ->
                        bookmarkedArticle.image == serverBlogArticle.image
                    }

                    Blog(
                        image = serverBlogArticle.image,
                        title = serverBlogArticle.title,
                        desc = serverBlogArticle.desc,
                        timestamp = serverBlogArticle.timestamp,
                        time = serverBlogArticle.time,
                        userName = serverBlogArticle.userName,
                        uid = serverBlogArticle.uid,
                        favorite = isBookmarked

                    )

                    }

            val blogs = newBlogs?.map { article ->
                LatestBlogs(article.image)

            }

            blogDataBase.withTransaction {

                blogDao.deleteAllBlogFeed()

                    if (newBlogs != null) {
                        blogDao.insertBlogs(newBlogs)
                    }

                if (blogs != null) {
                    blogDao.insertBlogFeed(blogs)
                }

            }
        },
        shouldFetch = { cachedArticles ->
            if (forceRefresh) {
                true
            } else {
                val sortedArticles = cachedArticles.sortedBy { article ->
                    article.time
                }
                val oldestTimestamp = sortedArticles.firstOrNull()?.time
                val needsRefresh = oldestTimestamp == null ||
                        oldestTimestamp < (System.currentTimeMillis() -
                        TimeUnit.MINUTES.toMillis(60)).toString()
                needsRefresh
            }
        },

        onFetchSuccess = onFetchSuccess,
        onFetchFailed = { t ->
            if (t !is HttpException && t !is IOException) {
                throw t
            }
            onFetchFailed(t)
        }

        )



    @ExperimentalCoroutinesApi
    fun fetchBlogPosts(

    ): Flow<List<Blog>> =  callbackFlow<List<Blog>> {
        val blogPostListener = object : ValueEventListener{

            override fun onCancelled(error: DatabaseError) {

            }

            override fun onDataChange(snapshot: DataSnapshot) {
                val items = snapshot.children.map { ds ->
                    ds.getValue(Blog::class.java)
                }
                this@callbackFlow.trySendBlocking(items.filterNotNull())
            }
        }
        blogRef
            .addValueEventListener(blogPostListener)

        awaitClose {
            blogRef
                .removeEventListener(blogPostListener)
        }


    }



    @ExperimentalCoroutinesApi
    fun searchForBlogs(str: String, forceRefresh: Boolean) =  callbackFlow<Resource<MutableList<Blog>>> {
        val queryBlog = blogRef.orderByChild("title")
            .startAt(str).
            endAt(str+"\uf8ff").addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    val queryList = mutableListOf<Blog>()
                    if (snapshot.exists()){
                        for (e in snapshot.children){
                            val  blog = e.getValue(Blog::class.java)
                            if (blog != null){
                                queryList.add(blog)
                            }
                        }
                        this@callbackFlow.trySendBlocking(Resource.Success(queryList))
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.w(TAG, "loadPost:onCancelled", error.toException())
                    this@callbackFlow.trySendBlocking(Resource.Error(error.toException(), null))
                }

            })

        blogRef
            .addValueEventListener( queryBlog)

        awaitClose {
            blogRef
                .removeEventListener( queryBlog)
        }

    }





    fun updateFavoriteStatus(id: String, isFavorite: Boolean) {
        blogRef.child(id).child("favorite").setValue(isFavorite)
    }




    fun getAllBookmarkedBlogs(): Flow<List<Blog>> =
        blogDao.getAllBookmarkedBlogs()

    suspend fun updateBlogs(blogs: Blog){
        blogDao.upsert(blogs)

    }

    suspend fun resetAllBookmarks() {
        blogDao.resetAllBookmarks()
    }

    suspend fun deleteNonBookmarkedArticlesOlderThan(timestampInMillis: Long) {
        blogDao.deleteNonBookmarkedArticlesOlderThan(timestampInMillis)
    }


    private suspend fun fetchBlogPost(): BlogResponse {
        val response = BlogResponse()
        try {
            response.blogs = blogRef.get().await().children.map { snapShot ->
                snapShot.getValue(Blog::class.java)!!
            }
        } catch (exception: Exception) {
            response.exception = exception
        }
        return response
    }





}



