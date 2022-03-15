package com.kanyandula.malawi.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.kanyandula.malawi.data.model.Blog
import com.kanyandula.malawi.data.model.LatestBlogs
import com.kanyandula.malawi.data.model.SearchResult

@Database(
    entities = [Blog::class, SearchResult::class, LatestBlogs::class, SearchQueryRemoteKey::class],
    version = 1
)

abstract class BlogDataBase : RoomDatabase(){

    abstract fun blogDao() : BlogDao
    abstract fun searchQueryRemoteKeyDao(): SearchQueryRemoteKeyDao
}