package com.kanyandula.malawi.data

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.google.firebase.database.DatabaseReference
import com.kanyandula.malawi.api.BlogResponse
import com.kanyandula.malawi.data.model.Blog
import com.kanyandula.malawi.data.model.SearchResult
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.tasks.await
import retrofit2.HttpException
import java.io.IOException
import java.lang.Exception

private const val NEWS_STARTING_PAGE_INDEX = 1

@OptIn(ExperimentalPagingApi::class)
class SearchNewsRemoteMediator(
    private val searchQuery: String,
    private var blogRef: DatabaseReference,
    private val blogArticleDb: BlogDataBase,
    private val refreshOnInit: Boolean
) : RemoteMediator<Int, Blog>() {

    private val newsArticleDao = blogArticleDb.blogDao()
    private val searchQueryRemoteKeyDao = blogArticleDb.searchQueryRemoteKeyDao()

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, Blog>
    ): MediatorResult {

        val page = when (loadType) {
            LoadType.REFRESH -> NEWS_STARTING_PAGE_INDEX
            LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
            LoadType.APPEND -> searchQueryRemoteKeyDao.getRemoteKey(searchQuery).nextPageKey
        }

        try {
            val response = searchBlogPost(searchQuery)
            val serverSearchResults = response.blogs

            val bookmarkedArticles = newsArticleDao.getAllBookmarkedBlogs().first()

            val searchResultArticles = serverSearchResults?.map { serverSearchResultArticle ->
                val isBookmarked = bookmarkedArticles.any { bookmarkedArticle ->
                    bookmarkedArticle.image == serverSearchResultArticle.image
                }

                Blog(
                    image = serverSearchResultArticle.image,
                    date = serverSearchResultArticle.date,
                    title = serverSearchResultArticle.title,
                    desc = serverSearchResultArticle.desc,
                    timestamp = serverSearchResultArticle.timestamp,
                    time = serverSearchResultArticle.time,
                    userName = serverSearchResultArticle.userName,
                    uid = serverSearchResultArticle.uid,
                    favorite = isBookmarked

                )
            }

            blogArticleDb.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    newsArticleDao.deleteSearchResultsForQuery(searchQuery)
                }

                val lastQueryPosition = newsArticleDao.getLastQueryPosition(searchQuery) ?: 0
                var queryPosition = lastQueryPosition + 1

                val searchResults = searchResultArticles?.map { article ->
                    SearchResult(searchQuery, article.image, queryPosition++)
                }

                val nextPageKey = page + 1

                if (searchResultArticles != null) {
                    newsArticleDao.insertBlogs(searchResultArticles)
                }
                if (searchResults != null) {
                    newsArticleDao.insertSearchResults(searchResults)
                }
                searchQueryRemoteKeyDao.insertRemoteKey(
                    SearchQueryRemoteKey(searchQuery, nextPageKey)
                )
            }
            return MediatorResult.Success(endOfPaginationReached =
                serverSearchResults!!.isEmpty()
             )

        } catch (exception: IOException) {
            return MediatorResult.Error(exception)
        } catch (exception: HttpException) {
            return MediatorResult.Error(exception)
        }
    }

    override suspend fun initialize(): InitializeAction {
        return if (refreshOnInit) {
            InitializeAction.LAUNCH_INITIAL_REFRESH
        } else {
            InitializeAction.SKIP_INITIAL_REFRESH
        }
    }

    private suspend fun searchBlogPost(str: String): BlogResponse {
        val response = BlogResponse()
        try {
            response.blogs = blogRef.orderByChild("title")
                .startAt(str).
                endAt(str+"\uf8ff").

            get().await().children.map { snapShot ->
                snapShot.getValue(Blog::class.java)!!
            }
        } catch (exception: Exception) {
            response.exception = exception
        }
        return response
    }



}