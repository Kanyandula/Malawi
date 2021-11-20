package com.kanyandula.malawi.utils

sealed class Resource1<T>(val data: T? = null, val message: String? =null) {
    class Success<T>(data: T) : Resource1<T>(data, null)
    class Error<T>(message: String) : Resource1<T>(null, message)
}