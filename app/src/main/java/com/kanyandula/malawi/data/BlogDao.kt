package com.kanyandula.malawi.data


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


        @Insert(onConflict = OnConflictStrategy.REPLACE)
        suspend fun insertBlogs(blogs: List<Blog>)


        @Query("SELECT * FROM blog_articles WHERE favorite = 1")
        fun getAllBookmarkedBlogs(): Flow<List<Blog>>


        @Query("SELECT * FROM blog_articles WHERE title LIKE '%' || :query || '%' OR desc LIKE '%' || :query || '%' ORDER BY date ")
        fun getSearchResultBlogPaged(query: String): PagingSource<Int, Blog>

        @Insert(onConflict = OnConflictStrategy.REPLACE)
        suspend fun insertBlogFeed(blogFeed: List<LatestBlogs>)

        @Insert(onConflict = OnConflictStrategy.REPLACE)
        suspend fun upsert(blog: Blog): Long


        @Query("SELECT MAX(queryPosition) FROM search_results WHERE searchQuery = :searchQuery")
        suspend fun getLastQueryPosition(searchQuery: String): Int?

        @Insert(onConflict = OnConflictStrategy.REPLACE)
        suspend fun insertSearchResults(searchResults: List<SearchResult>)


        @Query("UPDATE blog_articles SET favorite = 0")
        suspend fun resetAllBookmarks()

        @Query("DELETE FROM search_results WHERE searchQuery = :query")
        suspend fun deleteSearchResultsForQuery(query: String)


        @Query("DELETE FROM  latest_blog")
        suspend fun deleteAllBlogFeed()

        @Query("DELETE FROM blog_articles WHERE  timestamp < :timestampInMillis AND favorite = 0")
        suspend fun deleteNonBookmarkedArticlesOlderThan(timestampInMillis: Long)



}