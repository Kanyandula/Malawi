package com.kanyandula.malawi.ui


import androidx.lifecycle.*
import com.kanyandula.malawi.data.model.Blog
import com.kanyandula.malawi.repository.BlogRepository
import com.kanyandula.malawi.utils.ConnectivityManager
import com.kanyandula.malawi.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject
@ExperimentalCoroutinesApi
@HiltViewModel
class BlogViewModel @Inject constructor(
    private val repository: BlogRepository,
    private val connectivityManager: ConnectivityManager,

    ) : ViewModel() {

    private val eventChannel = Channel<Event>()
    val events = eventChannel.receiveAsFlow()

    private val refreshTriggerChannel = Channel<Refresh>()
    private val refreshTrigger = refreshTriggerChannel.receiveAsFlow()

    var pendingScrollToTopAfterRefresh = false


    val fetchBlogPost = refreshTrigger.flatMapLatest { refresh ->

        repository.fetchBlogPosts(
            refresh == Refresh.FORCE,
            onFetchSuccess = {
                pendingScrollToTopAfterRefresh = true
            },
            onFetchFailed = { t ->
                viewModelScope.launch { eventChannel.send(Event.ShowErrorMessage(t)) }
            }

        )



    }.stateIn(viewModelScope, SharingStarted.Lazily, null)

    init {

    }

    @ExperimentalCoroutinesApi
    fun onStart() {
        if (fetchBlogPost.value !is Resource.Loading) {
            viewModelScope.launch {
                refreshTriggerChannel.send(Refresh.NORMAL)
            }
        }
    }
    @ExperimentalCoroutinesApi
    fun onManualRefresh() {
        if (fetchBlogPost.value !is Resource.Loading) {
            viewModelScope.launch {
                refreshTriggerChannel.send(Refresh.FORCE)
            }
        }
    }

    fun onBookmarkClick(blog: Blog) {
        val currentlyBookmarked = blog.favorite
        val updatedArticle = blog.copy(favorite = !currentlyBookmarked)
        viewModelScope.launch {
            repository.updateBlogs(updatedArticle)

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



//    val responseLiveData = liveData(Dispatchers.IO) {
//        emit(repository.getResponseFromRealtimeDatabaseUsingCoroutines())
//    }


    enum class Refresh {
        FORCE, NORMAL
    }

    sealed class Event {
        data class ShowErrorMessage(val error: Throwable) : Event()
    }

//    fun updateFavoriteStatus(id: String, isFavorite: Boolean) {
//        repository.updateFavoriteStatus(id, isFavorite)
//    }

    fun updateFavoriteStatus(id: String, isFavorite: Boolean) {
        repository.updateFavoriteStatus(id, isFavorite)
    }
}