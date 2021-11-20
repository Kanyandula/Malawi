package com.kanyandula.malawi.repository

import androidx.lifecycle.MutableLiveData
import com.google.android.gms.common.api.Response
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.Query
import com.kanyandula.malawi.data.BlogResponse
import com.kanyandula.malawi.data.models.Blog
import com.kanyandula.malawi.utils.Constants.BLOG_REF
import com.kanyandula.malawi.utils.Constants.QUERY_PAGE_SIZE
import kotlinx.coroutines.tasks.await


class BlogRepository(
    private val rootRef: DatabaseReference = FirebaseDatabase.getInstance().reference,
    private val blogRef: DatabaseReference = rootRef.child(BLOG_REF)
    


) {
      fun getResponseFromRealtimeDatabaseUsingLiveData() : MutableLiveData<BlogResponse> {
        val mutableLiveData = MutableLiveData<BlogResponse>()
          blogRef
              .get().addOnCompleteListener { task ->
            val response = BlogResponse()
            if (task.isSuccessful) {
                val result = task.result
                result?.let {
                    response.Blog = result.children.map { snapShot ->
                        snapShot.getValue(Blog::class.java)!!
                    }
                }
            } else {
                response.exception = task.exception
            }
            mutableLiveData.value = response
        }
        return mutableLiveData
    }

    suspend fun getResponseFromRealtimeDatabaseUsingCoroutines(): BlogResponse {
        val response = BlogResponse()
        try {
            response.Blog = blogRef.get().await().children.map { snapShot ->
                snapShot.getValue(Blog::class.java)!!
            }
        } catch (exception: Exception) {
            response.exception = exception
        }
        return response
    }


}