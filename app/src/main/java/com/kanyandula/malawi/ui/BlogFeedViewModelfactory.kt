package com.kanyandula.malawi.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.kanyandula.malawi.repository.BlogRepository
import com.kanyandula.malawi.repository.FeedRepository
import com.kanyandula.malawi.utils.DispatcherProvider

class BlogFeedViewModelfactory(private val blogFeedRepository: FeedRepository,  private val dispatchers: DispatcherProvider)
    :ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return NewViewModel(blogFeedRepository,dispatchers) as T
    }

}