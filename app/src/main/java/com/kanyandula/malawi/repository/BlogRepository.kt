package com.kanyandula.malawi.repository


import androidx.room.withTransaction
import com.google.firebase.database.DatabaseReference
import com.kanyandula.malawi.api.BlogDto
import com.kanyandula.malawi.api.BlogResponse
import com.kanyandula.malawi.data.Blog
import com.kanyandula.malawi.data.BlogDataBase
import com.kanyandula.malawi.data.BlogFeed
import com.kanyandula.malawi.utils.Constants.BLOG_REF
import com.kanyandula.malawi.utils.Resource
import com.kanyandula.malawi.utils.networkBoundResource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

import kotlinx.coroutines.tasks.await
import java.lang.Exception
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BlogRepository @Inject constructor(
    //private val blogApi: BlogApi,
    private val blogRef: DatabaseReference,
   // private val : DatabaseReference = rootRef.child(BLOG_REF),
    private val blogDataBase: BlogDataBase
) {

    private val blogDao = blogDataBase.blogDao()


    fun getBlogs(
        forceRefresh: Boolean,
        onFetchSuccess: () -> Unit,
        onFetchFailed: (Throwable) -> Unit,
        blogDto: List<BlogDto>
    ): Flow<Resource<List<Blog>>> =
        networkBoundResource(
            query = {
                blogDao.getAllBlogFeed()
            },
            fetch = {
                val response = getResponseFromRealtimeDatabaseUsingCoroutines(blogDto)
                    response.blog

            },
            saveFetchResult = { serverBlogArticles ->
                val bookmarkedArticles = blogDao.getAllBookmarkedBlogs().first()

                val blogFeedsArticles =
                    serverBlogArticles.map { serverBlogArticle ->
                        val isBookmarked = bookmarkedArticles.any { bookmarkedArticle ->
                            bookmarkedArticle.time == serverBlogArticle.time

                        }

                        Blog(
                            date = serverBlogArticle.date,
                            desc =serverBlogArticle.desc,
                            image =serverBlogArticle.image,
                            time = serverBlogArticle.time,
                            title = serverBlogArticle.title,
                            uid = serverBlogArticle.uid,
                            userName = serverBlogArticle.userName,
                            rank = serverBlogArticle.rank,
                            isBookmarked = isBookmarked
                        )
                    }
                val blogsFeed = blogFeedsArticles?.map { blog ->
                    BlogFeed(
                        blog.time,
                        blog.desc,
                        blog.image,
                        blog.date,
                        blog.title,
                        blog.uid,
                        blog.userName,
                    blog.userName
                    )
                }

                blogDataBase.withTransaction {
                    blogDao.deleteAllBlogFeed()
                    blogDao.insertBlogs(blogFeedsArticles)
                    blogDao.insertBlogFeed(blogsFeed)
                }

            }
        )

    fun getAllBookMarkedBlogs(): Flow<List<Blog>> =
        blogDao.getAllBookmarkedBlogs()

    suspend fun  updateBlogsArticle(blog: Blog){
        blogDao.updateArticle(blog)
    }

    suspend fun resetAllBookmarks() {
        blogDao.resetAllBookmarks()
    }

    suspend fun deleteNonBookmarkedBlogsOlderThan(timestampInMillis: Long) {
        blogDao.deleteNonBookmarkedArticlesOlderThan(timestampInMillis)
    }



    private suspend fun getResponseFromRealtimeDatabaseUsingCoroutines( blog: List<BlogDto>): BlogResponse {

        val response = BlogResponse(blog)
        try {
            response.blog = blogRef.get().await().children.map { snapShot ->
                snapShot.getValue(BlogDto::class.java)!!
            }
        } catch (exception: Exception) {
            response.exception = exception
        }
        return response
    }



}