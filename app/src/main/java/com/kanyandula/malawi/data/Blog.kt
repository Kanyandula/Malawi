package com.kanyandula.malawi.data


import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable


@Entity(tableName = "blog_articles")
data class Blog(
    val date: String? = null ,
    val title: String? = null,
    val desc: String? = null,
    @PrimaryKey  @NonNull val image: String = "",
    val time: String? = null,
    val uid: String? = null,
    val userName: String? = null,
    val timestamp: String? = null,
    val favorite: Boolean = false
    ): Serializable{

}


@Entity(tableName = "blog_feed", primaryKeys = ["searchQuery", "title"]  )
data class BlogFeed(
    val searchQuery: String,
    val title: String,
    val queryPosition: Int


)