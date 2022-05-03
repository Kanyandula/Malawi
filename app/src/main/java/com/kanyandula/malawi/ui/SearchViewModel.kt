package com.kanyandula.malawi.ui

import androidx.lifecycle.*
import androidx.paging.cachedIn
import com.kanyandula.malawi.data.model.Blog
import com.kanyandula.malawi.repository.BlogRepository
import com.kanyandula.malawi.utils.Resource
import dagger.assisted.Assisted
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@ExperimentalCoroutinesApi
@HiltViewModel
class SearchViewModel @Inject constructor(
    private val repository: BlogRepository,
     state: SavedStateHandle
) : ViewModel() {

//    private val eventChannel = Channel<Event>()
//    val events = eventChannel.receiveAsFlow()
//    private val refreshTriggerChannel = Channel<Refresh>()
//    private val refreshTrigger = refreshTriggerChannel.receiveAsFlow()


    private  val currentQuery = state.getLiveData<String?>("currentQuery", "city")
    val hasCurrentQuery = currentQuery.asFlow().map { it != null }

    private var refreshOnInit = false

    val searchBlogPost = currentQuery.switchMap { queryString ->
        repository.getSearchResults(queryString, refreshOnInit).cachedIn(viewModelScope)

    }

    fun searchBlog(query: String) {
        refreshOnInit = true
        currentQuery.value = query
        newQueryInProgress = true
        pendingScrollToTopAfterNewQuery = true
    }


    @ExperimentalCoroutinesApi
    val searchResults = currentQuery.asFlow().flatMapLatest{ query ->
        query?.let {

            repository.getSearchResultsPaged(query,refreshOnInit)
        }?: emptyFlow()

    }.cachedIn(viewModelScope)



    var refreshInProgress = false
    var pendingScrollToTopAfterRefresh = false

    var newQueryInProgress = false
    var pendingScrollToTopAfterNewQuery = false

    fun onSearchQuerySubmit(query: String) {
        refreshOnInit = true
        currentQuery.value = query
        newQueryInProgress = true
        pendingScrollToTopAfterNewQuery = true
    }

    fun  onBookMarkClick (blog: Blog){
        val currentBookmarked = blog.favorite
        val updateBlog = blog.copy( favorite = !currentBookmarked)
        viewModelScope.launch {
            repository.updateBlogs(updateBlog)
        }
    }


    private val _blogFeedLiveData = MutableLiveData<List<Blog>>()
    val blogFeedLiveData : LiveData<List<Blog>> = _blogFeedLiveData

//    fun fetchFeed(search:String){
//        repository.searchForBlogs(search,_blogFeedLiveData)
//    }

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



    private enum class Refresh {
        FORCE, NORMAL
    }


    sealed class Event {
        data class ShowErrorMessage(val error: Throwable) : Event()
    }


    fun updateFavoriteStatus(id: String, isFavorite: Boolean) {
        repository.updateFavoriteStatus(id, isFavorite)
    }


    fun searchDatabase(searchQuery: String): LiveData<List<Blog>> {
        return repository.searchDatabase(searchQuery).asLiveData()
    }

}
