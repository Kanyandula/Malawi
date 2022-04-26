package com.kanyandula.malawi.api





data class BlogDto (

    var date: String? = null,
    val title: String? = null,
    val desc: String? = null,
   val image: String = "",
    val time: String? = null,
    val name: String? = null,
    val uid: String? = null,
    val userName: String? = null,
    val timestamp:Long = System.currentTimeMillis(),
    val favorite: Boolean = false,

        )