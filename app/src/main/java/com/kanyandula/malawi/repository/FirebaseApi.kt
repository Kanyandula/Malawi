package com.kanyandula.malawi.repository



import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.kanyandula.malawi.api.BlogDto
import com.kanyandula.malawi.api.BlogResponse
import com.kanyandula.malawi.data.Blog
import com.kanyandula.malawi.utils.Resource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await


interface FirebaseApi {

    companion object {
        lateinit var blogRef: DatabaseReference
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    suspend fun fetchBlogPosts()  = callbackFlow<List<Blog>> {
        val blogPostListener = object : ValueEventListener {

            override fun onCancelled(error: DatabaseError) {
                this@callbackFlow.trySendBlocking(error("Failed"))
            }

            override fun onDataChange(snapshot: DataSnapshot) {

                val queryList = mutableListOf<Blog>()
                if (snapshot.exists()) {
                    for (e in snapshot.children) {
                        val blog = e.getValue(Blog::class.java)
                        if (blog != null) {
                            queryList.add(blog)
                        }
                    }
                    this@callbackFlow.trySendBlocking(queryList)
                }


            }
        }
        blogRef
            .addValueEventListener(blogPostListener)

        awaitClose {
            blogRef
                .removeEventListener(blogPostListener)
        }
    }

    suspend fun fetchBlogPost1(): BlogResponse {
        val response = BlogResponse()
        try {
            response.blog = blogRef.get().await().children.map { snapShot ->
                snapShot.getValue(BlogDto::class.java)!!
            }
        } catch (exception: Exception) {
            response.exception = exception
        }
        return response
    }

    fun fetchBlogPost() : MutableLiveData<BlogResponse> {
        val mutableLiveData = MutableLiveData<BlogResponse>()
        blogRef.get().addOnCompleteListener { task ->
            val response = BlogResponse()
            if (task.isSuccessful) {
                val result = task.result
                result?.let {
                    response.blog = result.children.map { snapShot ->
                        snapShot.getValue(BlogDto::class.java)!!
                    }
                }
            } else {
                response.exception = task.exception
            }
            mutableLiveData.value = response
        }
        return mutableLiveData
    }

}