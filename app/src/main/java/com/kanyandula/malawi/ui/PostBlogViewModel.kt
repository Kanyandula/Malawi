package com.kanyandula.malawi.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.common.api.Response
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.ktx.Firebase
import com.kanyandula.malawi.api.BlogResponse
import com.kanyandula.malawi.data.Blog
import com.kanyandula.malawi.utils.Constants.BLOG_REF
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

@ExperimentalCoroutinesApi
@HiltViewModel
class PostBlogViewModel @Inject constructor(
    databaseAuth: FirebaseAuth,
    private var blogRef: DatabaseReference
) : ViewModel() {



     val user = databaseAuth.currentUser?.uid.toString()

    var blogs = MutableLiveData<List<Blog>>()
    val itemDeleted = MutableLiveData<Boolean>(false)

     fun fetchMovies(): LiveData<BlogResponse> {
         val mutableLiveData = MutableLiveData<BlogResponse>()
         blogRef.get().addOnCompleteListener { task ->
         val response = BlogResponse()
             if (task.isSuccessful) {
                 val result = task.result
                 result.let {
                     if (result != null) {
                         response.blog = result.children.map {
                             it.getValue(Blog::class.java)!!
                         }
                     }
                     }
                 } else{
                 response.exception = task.exception
                 }
             mutableLiveData.value = response
             }
           return  mutableLiveData

        }


    fun signOut() {
       Firebase.auth.signOut()


    }



    fun deleteMovie(id: String?) {
        itemDeleted.value = false
        blogRef.child(user).child(id.toString())
            .removeValue()
            .addOnCompleteListener() { task ->
                if (task.isSuccessful) {
                    fetchMovies()
                    itemDeleted.value = true
                } else {
                    Log.e("error", "error")
                }
            }
    }


}