package com.kanyandula.malawi.data


import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "blog_articles")
data class Blog(
    val date: String ,
    val desc: String ,
    val rank: String,
    val image: String,
    @PrimaryKey val time:String,
    val title: String ,
    val uid: String ,
    val userName: String ,
    val isBookmarked: Boolean,
    val updatedAt: Long = System.currentTimeMillis()

)

@Entity(tableName = "blog_feed")
data class BlogFeed(
    val date: String ,
    val desc: String ,
    val image: String,
    val time:String ,
    val title: String ,
    val rank: String,
    val uid: String ,
    val userName: String ,
    val updatedAt: Long = System.currentTimeMillis(),
    @PrimaryKey(autoGenerate = true) val id: Int = 0
)