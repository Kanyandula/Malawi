package com.kanyandula.malawi.data

import com.kanyandula.malawi.data.models.Blog

data class BlogResponse(
    var Blog:List<Blog> ? = null,
    var exception: Exception? = null,
    val totalResults: Int = 5000
)
