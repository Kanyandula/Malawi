package com.kanyandula.malawi.ui

import android.util.Log
import androidx.lifecycle.*
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.kanyandula.malawi.data.BlogResponse
import com.kanyandula.malawi.data.models.Blog
import com.kanyandula.malawi.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.IOException
import java.lang.Exception

class NewViewModel : ViewModel() {

    val database = FirebaseDatabase.getInstance().reference
    var  blogList = ArrayList<Blog>()
    val  blogList1 = MutableLiveData<Resource<List<Blog>>>()
    val _blogList : LiveData<Resource<List<Blog>>> =blogList1


    init {

        getBlogs( "")

    }

    fun getBlogs(KEYVALUE: String?) = viewModelScope.launch {
        getPosts(KEYVALUE)
    }


    private  fun getPosts(KEYVALUE: String?) {

        blogList1.postValue(Resource.loading(null))
                val postListener = object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        for (snapshot in snapshot.children) {
                            val res = snapshot.getValue(Blog::class.java)
                            Log.d("dataAdd", "Adding: ${res?.userName}")
                          //  blogList1.add(res!!)
                        }

                    }

                    override fun onCancelled(error: DatabaseError) {
                        // Getting Post failed, log a message
                        Log.w("readList", "loadPost:onCancelled", error.toException())
                        throw error.toException()
                    }
                }
                    try {
            if (KEYVALUE != null) {
                database.child("Blog").child(KEYVALUE).addValueEventListener(postListener)
            } else {
               // blogList1.postValue(Resource.success())
            }

        }catch (t: Throwable){
            when(t){

                is IOException ->   blogList1.postValue(Resource.error(
                    "error", null))
            }

        }


            }







    fun getData(KEYVALUE: String?) = liveData(Dispatchers.Main.immediate){
        val postListener = object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                for (snapshot in snapshot.children) {
                    val res = snapshot.getValue(Blog::class.java)
                    Log.d("dataAdd", "Adding: ${res?.userName}")
                    blogList.add(res!!)
                }

            }

            override fun onCancelled(error: DatabaseError) {
                // Getting Post failed, log a message
                Log.w("readList", "loadPost:onCancelled", error.toException())
                throw error.toException()
            }

        }

        try {
            if (KEYVALUE != null) {
                database.child("Blog").child(KEYVALUE).addValueEventListener(postListener)
            }
            emit(Resource.success(blogList))
        }catch (e: Exception){
            emit(Resource.error(
                "error",
                e.message ?: "Unknown Error"
            ))
        }

    }
}