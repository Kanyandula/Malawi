package com.kanyandula.malawi.api

import com.kanyandula.malawi.data.model.Blog


data class BlogResponse(
    var Blog:List<BlogDto>,
    var exception: Exception? = null,
    val totalResults: Int = 5000
)
