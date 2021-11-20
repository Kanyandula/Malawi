package com.kanyandula.malawi.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.kanyandula.malawi.data.models.Blog
import com.kanyandula.malawi.utils.Resource
import kotlinx.coroutines.flow.Flow

class FeedRepository {
    private val database = Firebase.database
    private val blogFeedReference = database.getReference("BLOG_REF")


    private val _blogFeedLiveData = MutableLiveData<List<Blog>>()


//    fun getFeed(
//        forceRefresh: Boolean,
//        onFetchSuccess: () -> Unit,
//        onFetchFailed: (Throwable) -> Unit
//    ): Flow<Resource<List<Blog>>> =

//    networkBoundResource(
//    query = {
//           null
//    },
//        fetch = {
//            fetchFeed(_blogFeedLiveData)
//        }
//
//
//    )

    fun fetchFeed(
    liveData: MutableLiveData<List<Blog>>)
    {
        blogFeedReference
            .orderByChild("rank")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val blogFeedItem: List<Blog> = snapshot.children.map { dataSnapshot ->
                        dataSnapshot.getValue(Blog::class.java)!!
                    }
                    Log.i("ITEM", snapshot.value.toString())
                    liveData.postValue(blogFeedItem)
                }

                override fun onCancelled(error: DatabaseError) {
                    // Nothing to do
                }

            })


    }
}
