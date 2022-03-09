package com.kanyandula.malawi.repository



import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.kanyandula.malawi.data.Blog
import com.kanyandula.malawi.utils.Resource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow


interface FirebaseApi {

    companion object {
        lateinit var blogRef: DatabaseReference
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    suspend fun fetchBlogPosts():  Flow<List<Blog>> =  callbackFlow<List<Blog>> {
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

}