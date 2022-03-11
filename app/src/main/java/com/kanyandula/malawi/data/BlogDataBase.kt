package com.kanyandula.malawi.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [Blog::class, SearchResult::class, LatestBlogs::class, SearchQueryRemoteKey::class],
    version = 1
)

abstract class BlogDataBase : RoomDatabase(){

    abstract fun blogDao() : BlogDao
    abstract fun searchQueryRemoteKeyDao(): SearchQueryRemoteKeyDao
}