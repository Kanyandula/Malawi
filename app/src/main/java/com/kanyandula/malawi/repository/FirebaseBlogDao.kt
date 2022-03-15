package com.kanyandula.malawi.repository



import com.kanyandula.malawi.data.model.Blog
import com.kanyandula.malawi.utils.Resource
import kotlinx.coroutines.flow.Flow


interface FirebaseBlogDao {

    fun fetchBlogPosts(): Flow<Resource<List<Blog>>>

}