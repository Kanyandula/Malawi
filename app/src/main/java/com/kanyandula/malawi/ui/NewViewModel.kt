package com.kanyandula.malawi.ui


import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.kanyandula.malawi.data.models.Blog
import com.kanyandula.malawi.repository.FeedRepository
import com.kanyandula.malawi.utils.DispatcherProvider
import com.kanyandula.malawi.utils.Resource1
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import com.kanyandula.malawi.utils.Resource1.Error as Error1

class NewViewModel(
    private val blogRepository: FeedRepository,
    private val dispatchers: DispatcherProvider
) : ViewModel() {

    sealed class BlogEvent {
        class Success(val resultText: String): BlogEvent()
        class Failure(val errorText: String): BlogEvent()
        object Loading : BlogEvent()
        object Empty : BlogEvent()
    }

    private val _feed = MutableStateFlow<BlogEvent>(BlogEvent.Empty)
    val feed: StateFlow<BlogEvent> = _feed



    /*******************************************************************************/
    private val eventChannel = Channel<Event>()
    val events = eventChannel.receiveAsFlow()

    private val refreshTriggerChannel = Channel<Refresh>()
    private val refreshTrigger = refreshTriggerChannel.receiveAsFlow()

    var pendingScrollToTopAfterRefresh = false

    private val _blogFeedLiveData = MutableLiveData<List<Blog>>()

    fun getBlogFeed()= blogRepository.fetchFeed(_blogFeedLiveData)

  //  val blogFeed = refreshTrigger.flatMapLatest { refresh ->
//       blogRepository.fetchFeed(
//           refresh == Refresh.FORCE,
//           onFetchSuccess = {
//             pendingScrollToTopAfterRefresh = true
//                         },
//           onFetchFailed = { t ->
//               viewModelScope.launch { eventChannel.send(Event.ShowErrorMessage(t)) }
//           }
//       )
  //  }.stateIn(viewModelScope, SharingStarted.Lazily, null)




    //{
//        viewModelScope.launch(dispatchers.io){
//        _feed.value =BlogEvent.Loading
//            when(val blogResponse = blogRepository.fetchFeed(_blogFeedLiveData)){
//                is Resource1.Error -> _feed.value = BlogEvent.Failure(blogResponse.message!!)
//                is
//
//            }
//        }

        enum class Refresh {
            FORCE, NORMAL
        }

        sealed class Event {
            data class ShowErrorMessage(val error: Throwable) : Event()
        }

    }







//   private val repository = FeedRepository()
//
//    private val _blogFeedLiveData = MutableLiveData<List<Blog>>()
//    val blogFeedLiveData: LiveData<List<Blog>> = _blogFeedLiveData
//
//    fun fetchBlogFeed(){
//        repository.fetchFeed(_blogFeedLiveData)
    //}

          //  }







