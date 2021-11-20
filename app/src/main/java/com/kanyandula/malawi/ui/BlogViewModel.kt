package com.kanyandula.malawi.ui


import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.lifecycle.*
import com.google.android.gms.common.api.Response
import com.kanyandula.malawi.MalawiBlogApplication
import com.kanyandula.malawi.data.BlogResponse
import com.kanyandula.malawi.data.models.Blog
import com.kanyandula.malawi.repository.BlogRepository
import com.kanyandula.malawi.utils.Resource
import kotlinx.coroutines.Dispatchers
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



    val responseLiveData = liveData(Dispatchers.IO) {
        emit(repository.getResponseFromRealtimeDatabaseUsingCoroutines())
    }



    private fun hasInternetConnection(): Boolean {
        val connectivityManager = getApplication<MalawiBlogApplication>().getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val activeNetwork = connectivityManager.activeNetwork ?: return false
            val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
            return when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                else -> false
            }
        } else {
            connectivityManager.activeNetworkInfo?.run {
                return when(type) {
                    ConnectivityManager.TYPE_WIFI -> true
                    ConnectivityManager.TYPE_MOBILE -> true
                    ConnectivityManager.TYPE_ETHERNET -> true
                    else -> false
                }
            }
        }
        return false
    }




}