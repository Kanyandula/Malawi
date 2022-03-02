//package com.kanyandula.malawi.data
//
//import androidx.room.*
//import kotlinx.coroutines.flow.Flow
//
//@Dao
//interface BlogDao {
//
//       // @Query("SELECT * FROM blog_articles ORDER BY rank ASC ")
//        fun getAllBlogFeed(): Flow<List<Blog>>
//
//      //  @Query("SELECT * FROM blog_articles WHERE isBookmarked = 1")
//        fun getAllBookmarkedBlogs(): Flow<List<Blog>>
//
//        @Insert(onConflict = OnConflictStrategy.REPLACE)
//        suspend fun insertBlogs(articles: List<Blog>)
//
//        @Insert(onConflict = OnConflictStrategy.REPLACE)
//        suspend fun insertBlogFeed(blogFeed: List<BlogFeed>)
//
//        @Update
//        suspend fun updateArticle(blog: Blog)
//
//      //  @Query("UPDATE blog_articles SET isBookmarked = 0")
//        suspend fun resetAllBookmarks()
//
//        @Query("DELETE FROM  blog_feed")
//        suspend fun deleteAllBlogFeed()
//
//      //  @Query("DELETE FROM blog_articles WHERE updatedAt < :timestampInMillis AND isBookmarked = 0")
//        suspend fun deleteNonBookmarkedArticlesOlderThan(timestampInMillis: Long)
//
//}