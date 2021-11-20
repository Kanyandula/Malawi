package com.kanyandula.malawi.utils

//data class Resource<out T>(val status: Status, val data: T?, val message: String?) {
//
//    companion object {
//        fun <T> success(data: T?) = Resource(Status.SUCCESS, data, null)
//
//        fun <T> error(message: String, data: T?) = Resource(Status.ERROR, data, message)
//
//        fun <T> loading(data: T?) = Resource(Status.LOADING, data, null)
//    }
//}
//
//enum class Status {
//    SUCCESS,
//    ERROR,
//    LOADING
//}

sealed class Resource<T>(
    val data: T? = null,
    val message: String? = null
) {
    class Success<T>(data: T) : Resource<T>(data)
    class Error<T>(message: String, data: T? = null) : Resource<T>(data, message)
    //class Loading<T> : Resource<T>()
    class Loading<T>(data: T? = null) : Resource<T>(data)
}

//sealed class Resource<T>(
//    val data: T? = null,
//    val error: Throwable? = null
//) {
//    class Success<T>(data: T) : Resource<T>(data)
//    class Loading<T>(data: T? = null) : Resource<T>(data)
//    class Error<T>(throwable: Throwable, data: T? = null) : Resource<T>(data, throwable)
//}