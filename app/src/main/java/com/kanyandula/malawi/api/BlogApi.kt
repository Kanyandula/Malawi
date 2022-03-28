package com.kanyandula.malawi.api


import retrofit2.http.GET


interface BlogApi {


    @GET("/Blog/.json")
    suspend fun getBreakingNews(): BlogResponse
}