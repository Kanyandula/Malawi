package com.kanyandula.malawi.data.models


import java.io.Serializable



data class Blog(
    val date: String ?= null,
    val desc: String ?= null,
    val image: String ?= null,
    val time:String ?= null,
    val title: String ?= null,
    val uid: String ?= null,
    val userName: String ?= null,
): Serializable{

}