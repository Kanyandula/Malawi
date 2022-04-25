package com.kanyandula.malawi.api

import com.kanyandula.malawi.data.model.Blog


data class BlogResponse(
    var blogs:List<Blog>? = null,
    var exception: Exception? = null,
    val totalResults: Int = 5000
)
