package com.kanyandula.malawi.repository


import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.bumptech.glide.load.HttpException
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.kanyandula.malawi.api.BlogDto
import com.kanyandula.malawi.api.BlogDtoMapper
import com.kanyandula.malawi.api.BlogResponse
import com.kanyandula.malawi.data.model.Blog
import com.kanyandula.malawi.data.BlogDataBase
import com.kanyandula.malawi.data.model.BlogEntityMapper
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
    private var blogRef: DatabaseReference,
    private  val blogDataBase: BlogDataBase,
    private val entityMapper: BlogEntityMapper,
    private val blogDtoMapper: BlogDtoMapper
): FirebaseBlogDao {




    private  val blogDao = blogDataBase.blogDao()

    fun getBlogPosts(
        forceRefresh: Boolean,
        onFetchSuccess: () -> Unit,
        onFetchFailed: (Throwable) -> Unit
    ): Flow<Resource<List<Blog>>> =
        networkBoundResource(
            query = {
                blogDao.getAllBlogFeed()
            },

            fetch = {
                val response = fetchBlogPost1()
                response.blog

            },

            saveFetchResult = {
                val result = fetchBlogPost1()
                result.blog

                result.blog?.let { it1 -> entityMapper.mapFromDomainModel(it1) }?.let { it2 ->
                    blogDao.insertBlogs(
                        it2
                    )
                }
            },

            shouldFetch = { cachedArticles ->
                if (forceRefresh) {
                    true
                } else {
                    val sortedArticles = cachedArticles.sortedBy { article ->
                        article.timestamp
                    }
                    val oldestTimestamp = sortedArticles.firstOrNull()?.timestamp
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





    @OptIn(ExperimentalCoroutinesApi::class)
    override fun fetchBlogPosts() =  callbackFlow<Resource<List<Blog>>> {
        val blogPostListener = object : ValueEventListener {

            override fun onCancelled(error: DatabaseError) {
                this@callbackFlow.trySendBlocking(Resource.Error(error.toException(), null))
            }

            override fun onDataChange(snapshot: DataSnapshot) {

                val queryList = mutableListOf<Blog>()
                if (snapshot.exists()) {
                    for (e in snapshot.children) {
                        val blog = e.getValue(Blog::class.java)
                        if (blog != null) {
                            queryList.add(blog)
                        }
                    }
                    this@callbackFlow.trySendBlocking(Resource.Success(queryList))
                }


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


    fun signIn(auth: FirebaseAuth, email: String, password: String): Flow<Resource<AuthResult>> {
        return flow {

            val result = auth.signInWithEmailAndPassword(email, password)
                .await()
            emit(Resource.Success(result))

        }

    }



    fun updateFavoriteStatus(id: String, isFavorite: Boolean) {
        blogRef.child(id).child("favorite").setValue(isFavorite)
    }

    /**  private suspend fun getResponseFromRealtimeDatabaseUsingCoroutines( blog: List<BlogDto>): BlogResponse {

    val response = BlogResponse(blog)
    try {
    response.blog = blogRef.get().await().children.map { snapShot ->
    snapShot.getValue(BlogDto::class.java)!!
    }
    } catch (exception: Exception) {
    response.exception = exception
    }
    return response
    } **/

      fun searchForBlogs(str: String, liveData: MutableLiveData<List<Blog>>) {

        blogRef.orderByChild("title")
            .startAt(str).
            endAt(str+"\uf8ff")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val blogFeedItems : List<Blog>  = snapshot.children.map { dataSnapshot ->
                        dataSnapshot.getValue(Blog::class.java)!!

                    }

                    liveData.postValue(blogFeedItems)
                }

                override fun onCancelled(errorDatabase: DatabaseError) {


                    // Nothing to do
                }

            })
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





    suspend fun fetchBlogPost1(): BlogResponse {
        val response = BlogResponse()
        try {
            response.blog = blogRef.get().await().children.map { snapShot ->
                snapShot.getValue(BlogDto::class.java)!!
            }
        } catch (exception: Exception) {
            response.exception = exception
        }
        return response
    }



    fun fetchBlogPost() : MutableLiveData<BlogResponse> {
        val mutableLiveData = MutableLiveData<BlogResponse>()
        blogRef.get().addOnCompleteListener { task ->
            val response = BlogResponse()
            if (task.isSuccessful) {
                val result = task.result
                result?.let {
                    response.blog = result.children.map { snapShot ->
                        snapShot.getValue(BlogDto::class.java)!!
                    }
                }
            } else {
                response.exception = task.exception
            }
            mutableLiveData.value = response
        }
        return mutableLiveData
    }





}

