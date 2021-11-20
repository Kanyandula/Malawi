package com.kanyandula.malawi.ui

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

import com.kanyandula.malawi.repository.BlogRepository

class BlogViewModelProviderFactory(
    val app: Application,
    private val blogRepository: BlogRepository
): ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
       return BlogViewModel(app, blogRepository) as T

    }


}