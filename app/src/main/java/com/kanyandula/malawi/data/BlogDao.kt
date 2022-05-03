package com.kanyandula.malawi.data

import androidx.lifecycle.LiveData
import androidx.paging.PagingSource
import androidx.room.*
import com.kanyandula.malawi.data.model.Blog
import com.kanyandula.malawi.data.model.LatestBlogs
import com.kanyandula.malawi.data.model.SearchResult
import kotlinx.coroutines.flow.Flow

@Dao
interface BlogDao {



        @Query("SELECT * FROM  latest_blog   INNER JOIN blog_articles  ON  articleUrl = image ")
        fun getAllBlogFeed(): Flow<List<Blog>>

        @Query("SELECT * FROM  blog_articles ")
        fun getAllBlogPost(): Blog

        @Insert
        suspend fun insertBlog(blog: Blog): Long

        @Insert(onConflict = OnConflictStrategy.REPLACE)
        suspend fun insertBlogs(blogs: List<Blog>)


        @Query("SELECT * FROM blog_articles WHERE favorite = 1")
        fun getAllBookmarkedBlogs(): Flow<List<Blog>>

        @Query("SELECT * FROM search_results INNER JOIN blog_articles ON articleUrl = image WHERE searchQuery = :query ORDER BY queryPosition")
        fun getSearchResultArticlesPaged(query: String): PagingSource<Int, Blog>

        @Query("SELECT * FROM blog_articles WHERE title LIKE '%' || :query || '%' OR  desc LIKE '%' || :query || '%' ORDER BY date ")
        fun getSearchResultBlogPaged(query: String): PagingSource<Int, Blog>

        @Query("SELECT * FROM blog_articles WHERE title LIKE :query OR desc LIKE :query")
        fun searchDatabase(query: String): Flow<List<Blog>>

        @Insert(onConflict = OnConflictStrategy.REPLACE)
        suspend fun insertBlogFeed(blogFeed: List<LatestBlogs>)

        @Insert(onConflict = OnConflictStrategy.REPLACE)
        suspend fun upsert(blog: Blog): Long

        @Query("SELECT * FROM  blog_articles")
        fun getAllArticles(): LiveData<List<Blog>>

        @Query("SELECT MAX(queryPosition) FROM search_results WHERE searchQuery = :searchQuery")
        suspend fun getLastQueryPosition(searchQuery: String): Int?

        @Insert(onConflict = OnConflictStrategy.REPLACE)
        suspend fun insertSearchResults(searchResults: List<SearchResult>)

        @Update
        suspend fun updateArticle(blog: Blog)

        @Query("UPDATE blog_articles SET favorite = 0")
        suspend fun resetAllBookmarks()

        @Query("DELETE FROM search_results WHERE searchQuery = :query")
        suspend fun deleteSearchResultsForQuery(query: String)


        @Query("DELETE FROM  latest_blog")
        suspend fun deleteAllBlogFeed()

        @Query("DELETE FROM blog_articles WHERE timestamp < :timestampInMillis AND favorite = 0")
        suspend fun deleteNonBookmarkedArticlesOlderThan(timestampInMillis: Long)

}