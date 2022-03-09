package com.kanyandula.malawi.api

import com.kanyandula.malawi.data.Blog


data class BlogResponse(
    var blog:List< BlogDto>? = null,
    var exception: Exception? = null,
   // val totalResults: Int = 5000
)
