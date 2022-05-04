package com.kanyandula.malawi.data.model


import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.io.Serializable


@Entity(tableName = "blog_articles")
data class Blog(

    var date: String? = null,
    val title: String? = null,
    val desc: String? = null,
    @PrimaryKey
    @NonNull
    var image: String = "",
    val time: String? = null,
    val uid: String? = null,
    val userName: String? = null,
    val timestamp: Long = System.currentTimeMillis(),
    var favorite: Boolean = false,

    ): Serializable{

}
@Entity(tableName = "latest_blog")
data class LatestBlogs(
    val articleUrl: String,
    val favorite: Boolean = false,
    @PrimaryKey(autoGenerate = true) val id: Int = 0

)


@Entity(tableName = "search_results", primaryKeys = ["searchQuery", "articleUrl"]  )
data class SearchResult(
    val searchQuery: String,
    val articleUrl: String,
    val queryPosition: Int


)