package com.kanyandula.malawi.data


import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.firebase.database.IgnoreExtraProperties
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
    val timestamp: String? = null,
    val favorite: Boolean = false
    ): Serializable{

}

@Entity(tableName = "breaking_news")
data class  HomeArticles(
    val date: String? = null ,
    val title: String? = null,
    val desc: String? = null,
    val time: String? = null,
    val uid: String? = null,
    val userName: String? = null,
    val timestamp: String? = null,
    val favorite: Boolean? = null,
    @PrimaryKey(autoGenerate = true) val id: Int = 0
)

@Entity(tableName = "blog_feed" )
data class BlogFeed(
    val date: String ,
    val desc: String ,
    @PrimaryKey @NonNull
    val image: String,
    val time:String ,
    val title: String ,
    val rank: String,
    val uid: String ,
    val userName: String ,


)