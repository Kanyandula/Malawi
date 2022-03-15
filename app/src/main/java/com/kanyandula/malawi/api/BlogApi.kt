//package com.kanyandula.malawi.api
//
//import com.google.firebase.database.DatabaseReference
//import com.google.firebase.database.FirebaseDatabase
//import com.kanyandula.malawi.data.model.Blog
//import com.kanyandula.malawi.utils.Constants
//import kotlinx.coroutines.tasks.await
//import java.lang.Exception
//
//interface BlogApi {
//
//    companion object {
//        private val rootRef: DatabaseReference = FirebaseDatabase.getInstance().reference
//        private val blogRef: DatabaseReference = rootRef.child(Constants.BLOG_REF)
//    }
//     suspend fun getResponseFromRealtimeDatabaseUsingCoroutines(blogDto: List<BlogDto> ): BlogResponse {
//        val response = BlogResponse(blogDto)
//        try {
//            response.blog = blogRef.get().await().children.map { snapShot ->
//                snapShot.getValue(BlogDto::class.java)!!
//            }
//        } catch (exception: Exception) {
//            response.exception = exception
//        }
//        return response
//    }
//}