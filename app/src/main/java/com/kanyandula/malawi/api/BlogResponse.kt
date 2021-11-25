package com.kanyandula.malawi.api



data class BlogResponse(
    var blog:List<BlogDto>,
    var exception: Exception? = null,
    val totalResults: Int = 5000
)
