package com.kanyandula.malawi.api


import com.kanyandula.malawi.data.model.Blog
import retrofit2.http.GET


interface BlogApi {


    @GET("v3/3aeee6d8-ce98-4950-9d05-a6fa5f66ac49" )
    suspend fun getBreakingNews(): NetworkResponse
}