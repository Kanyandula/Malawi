package com.kanyandula.malawi.ui

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
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



    private val user = databaseAuth.uid.toString()

    var blogs = MutableLiveData<List<Blog>>()
    val itemDeleted = MutableLiveData<Boolean>(false)

     fun fetchMovies() {

         blogRef.child(user).child(BLOG_REF).get().addOnSuccessListener {
            val blogList: MutableList<Blog> = mutableListOf()
            var lastChild = it.childrenCount

            var i = 0
            while (i < lastChild) {
                if (!it.child(i.toString()).exists()) {
                    lastChild++
                }
                i++
            }

            for (j in 0 until lastChild) {
                val singleBlog = it.child(j.toString()).getValue(Blog::class.java)
                singleBlog?.let { blog -> blogList.add(blog) }
            }

            blogs.value = blogList
        }
    }


    fun deleteMovie(id: String?) {
        itemDeleted.value = false
        blogRef.child(user).child(BLOG_REF).child(id.toString())
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