package com.kanyandula.malawi.repository


import androidx.paging.*
import androidx.room.withTransaction
import com.bumptech.glide.load.HttpException
import com.google.firebase.database.*
import com.kanyandula.malawi.api.BlogApi
import com.kanyandula.malawi.api.BlogResponse
import com.kanyandula.malawi.data.model.Blog
import com.kanyandula.malawi.data.BlogDataBase
import com.kanyandula.malawi.data.model.LatestBlogs
import com.kanyandula.malawi.utils.Resource
import com.kanyandula.malawi.utils.networkBoundResource
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.tasks.await
import java.io.IOException
import java.lang.Exception
import java.util.concurrent.TimeUnit
import javax.inject.Inject



class BlogRepository @Inject constructor(
    private var blogRef: DatabaseReference,
    private var blogApi: BlogApi,
    private val   blogDataBase: BlogDataBase,
){
     private val blogDao = blogDataBase.blogDao()

    fun getBlogFeed(

        forceRefresh: Boolean,
        onFetchSuccess: () -> Unit,
        onFetchFailed: (Throwable) -> Unit,
    ): Flow<Resource<List<Blog>>> = networkBoundResource(
        query = {
            blogDao.getAllBlogFeed()
        },
        fetch = {

                val response = blogApi.getBreakingNews()
                response.blogs

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
                        date = serverBlogArticle.date,
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

               // blogDao.deleteAllBlogFeed()

                    if (newBlogs != null) {
                        blogDao.insertBlogs(newBlogs)
                    }

                if (blogs != null) {
                    blogDao.insertBlogFeed(blogs)
                }
            }
        },
        shouldFetch = { cachedArticles ->
            if ( forceRefresh) {
                true
            } else {
                val sortedArticles = cachedArticles.sortedBy { article ->
                    article.timestamp
                }
                val oldestTimestamp = sortedArticles.firstOrNull()?.timestamp
                val needsRefresh = oldestTimestamp == null ||
                        oldestTimestamp < System.currentTimeMillis() -
                        TimeUnit.MINUTES.toMillis(60)
                needsRefresh
            }
        },
        onFetchSuccess = onFetchSuccess,
        onFetchFailed = { t ->
            if (t !is HttpException && t !is IOException) {
                throw t
            }
            onFetchFailed(t)
        },


        )

    @OptIn(ExperimentalPagingApi::class)
    fun getSearchResultsPaged(
        query: String,
    ): Flow<PagingData<Blog>> =
        Pager(
            config = PagingConfig(pageSize = 20, maxSize = 200),
            pagingSourceFactory = { blogDao.getSearchResultBlogPaged(query) }
        ).flow





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



