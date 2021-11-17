package com.kanyandula.malawi.ui


import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.android.gms.common.api.Response
import com.kanyandula.malawi.data.BlogResponse
import com.kanyandula.malawi.data.models.Blog
import com.kanyandula.malawi.repository.BlogRepository
import com.kanyandula.malawi.utils.Resource
import kotlinx.coroutines.launch


class BlogViewModel(
    app: Application,
    private val repository: BlogRepository
) : AndroidViewModel(app) {
    
    val blogPosts : MutableLiveData <Resource<BlogResponse>> = MutableLiveData()
    var blogPage = 1
    var blogBlogResponse : BlogResponse? = null


    private val maxRecentlyViewed = 5
    val recentlyViewed = ArrayList<Blog>(maxRecentlyViewed)

    fun addToRecentlyViedBlogs(blog: Blog){
        val existingIndex = recentlyViewed.indexOf(blog)
        if (existingIndex == -1){
            recentlyViewed.add(0,blog)
            for (index in recentlyViewed.lastIndex downTo maxRecentlyViewed)
                recentlyViewed.removeAt(index)
        } else {

            // it is in the list...
            // Shift the ones above down the list and make it first member of the list
            for (index in (existingIndex - 1) downTo 0)
                recentlyViewed[index + 1] = recentlyViewed[index]
            recentlyViewed[0] = blog
        }
    }
    

    init {
        getResponseUsingLiveData()

    }



    fun getResponseUsingLiveData() : MutableLiveData<BlogResponse> {
        return repository.getResponseFromRealtimeDatabaseUsingLiveData()
    }




     fun getBlogsPost() = viewModelScope.launch {

     }

//    private fun handleBlogPostResponse (response : ) : Resource<BlogResponse>{
//
//        if (response.isSuccessful)
//
//    }


//    private suspend fun  safeBlogPostCall (){
//
//        blogPosts.postValue(Resource.Loading())
//        val  response = repository.getResponseFromRealtimeDatabaseUsingLiveData()
//        blogPosts.postValue()
//    }

}