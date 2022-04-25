package com.kanyandula.malawi.data

import androidx.lifecycle.LiveData
import androidx.paging.PagingSource
import androidx.room.*
import com.kanyandula.malawi.data.model.Blog
import com.kanyandula.malawi.data.model.LatestBlogs
import kotlinx.coroutines.flow.Flow

@Dao
interface BlogDao {



        @Query("SELECT * FROM  blog_articles   ")
        fun getAllBlogFeed(): Flow<List<Blog>>

        @Query("SELECT * FROM  blog_articles ")
        fun getAllBlogPost(): Blog

        @Insert
        suspend fun insertBlog(blog: Blog): Long

        @Insert(onConflict = OnConflictStrategy.IGNORE)
        suspend fun insertBlogs(blogs: List<Blog>)


        @Query("SELECT * FROM blog_articles WHERE favorite = 1")
        fun getAllBookmarkedBlogs(): Flow<List<Blog>>

//        @Query("SELECT * FROM search_results INNER JOIN blog_articles ON title = image WHERE searchQuery = :query ORDER BY queryPosition")
//        fun getSearchResultArticlesPaged(query: String): PagingSource<Int, Blog>

        @Insert(onConflict = OnConflictStrategy.REPLACE)
        suspend fun insertBlogFeed(blogFeed: List<LatestBlogs>)

        @Insert(onConflict = OnConflictStrategy.REPLACE)
        suspend fun upsert(blog: Blog): Long

        @Query("SELECT * FROM  blog_articles")
        fun getAllArticles(): LiveData<List<Blog>>

        @Update
        suspend fun updateArticle(blog: Blog)

        @Query("UPDATE blog_articles SET favorite = 0")
        suspend fun resetAllBookmarks()

        @Query("DELETE FROM  search_results")
        suspend fun deleteAllBlogFeed()

        @Query("DELETE FROM blog_articles WHERE timestamp < :timestampInMillis AND favorite = 0")
        suspend fun deleteNonBookmarkedArticlesOlderThan(timestampInMillis: Long)

}