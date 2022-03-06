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
    @PrimaryKey @NonNull
    val image: String = "",
    val time: String? = null,
    val uid: String? = null,
    val userName: String? = null,

    ): Serializable{

}

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