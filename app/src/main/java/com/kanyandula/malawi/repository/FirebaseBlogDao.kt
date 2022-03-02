package com.kanyandula.malawi.repository

import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.kanyandula.malawi.data.Blog
import com.kanyandula.malawi.utils.Resource
import kotlinx.coroutines.flow.Flow

interface FirebaseBlogDao {

    fun fetchBlogPosts(forceRefresh: Boolean): Flow<Resource<List<Blog>>>
    //fun signIn(auth: FirebaseAuth, email: String, password: String): Flow<Resource<AuthResult>>
}