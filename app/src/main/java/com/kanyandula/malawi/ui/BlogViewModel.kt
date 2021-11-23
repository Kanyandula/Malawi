package com.kanyandula.malawi.ui


import androidx.lifecycle.*
import com.kanyandula.malawi.data.models.Blog
import com.kanyandula.malawi.repository.BlogRepository
import kotlinx.coroutines.Dispatchers


class BlogViewModel @JvmOverloads constructor(

    private val repository: BlogRepository = BlogRepository()


    ) : ViewModel() {



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



    val responseLiveData = liveData(Dispatchers.IO) {
        emit(repository.getResponseFromRealtimeDatabaseUsingCoroutines())
    }



}