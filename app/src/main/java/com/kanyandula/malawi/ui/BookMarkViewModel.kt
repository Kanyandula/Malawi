package com.kanyandula.malawi.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kanyandula.malawi.data.model.Blog
import com.kanyandula.malawi.repository.BlogRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookMarkViewModel @Inject constructor(
    private val repository: BlogRepository
):ViewModel() {

    val bookmarks = repository.getAllBookmarkedBlogs()
        .stateIn(viewModelScope, SharingStarted.Lazily,null)

    fun  onBookMarkClick (blog: Blog){
        val currentBookmarked = blog.favorite
        val updateBlog = blog.copy( favorite = !currentBookmarked)
        viewModelScope.launch {
            repository.updateBlogs(updateBlog)
        }
    }

    fun onDeleteAllBookmarks() {
        viewModelScope.launch {
            repository.resetAllBookmarks()
        }
    }

    private val maxRecentlyViewed = 5
    private val recentlyViewed = ArrayList<Blog>(maxRecentlyViewed)

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
    fun updateFavoriteStatus(id: String, favorite: Boolean) {
        repository.updateFavoriteStatus(id, favorite)
    }

}