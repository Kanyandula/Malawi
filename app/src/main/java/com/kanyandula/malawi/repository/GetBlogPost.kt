//package com.kanyandula.malawi.repository
//
//
//import android.util.Log
//import com.google.firebase.database.DatabaseReference
//import com.kanyandula.malawi.api.BlogDto
//import com.kanyandula.malawi.api.BlogResponse
//import com.kanyandula.malawi.data.BlogDao
//import com.kanyandula.malawi.data.DataState
//import com.kanyandula.malawi.data.model.Blog
//import com.kanyandula.malawi.data.model.BlogEntityMapper
//import com.kanyandula.malawi.utils.Constants.TAG
//import kotlinx.coroutines.delay
//import kotlinx.coroutines.flow.Flow
//import kotlinx.coroutines.flow.flow
//import kotlinx.coroutines.tasks.await
//import java.lang.Exception
//
//class GetBlogPost(
//    private var blogRef: DatabaseReference,
//    private val blogDao: BlogDao,
//    private val entityMapper: BlogEntityMapper,
//    //private val blogDtoMapper: BlogDtoMapper
//) {
//
//    fun execute(
//        isNetworkAvailable: Boolean,
//    ): Flow<DataState<BlogDto>> = flow {
//
//        try {
//            emit(DataState.loading<BlogDto>())
//            delay(1000)
//
//            var blog =  getBlogFromCache()
//            if (blog != null){
//                emit(DataState.success(blog))
//            }else{
//                if (isNetworkAvailable) {
//
//                    val networkBlog = fetchBlogPost()
//
//                    //insert into cache
//                    networkBlog.Blog?.forEach {
//                            e ->
//                        blogDao.insertBlog(e)
//
//                    }
//
//
//
//
//                }
//                blog = getBlogFromCache()
//                if (blog != null){
//                    emit(DataState.success(blog))
//                }
//                else{
//                    throw  Exception("Unable to get blog from cache.")
//                }
//            }
//
//        }catch (e:Exception){
//            Log.e(TAG, "execute: ${e.message}")
//            emit(DataState.error(e.message?: "Unknown error"))
//        }
//
//
//    }
//
//
//    private suspend fun getBlogFromCache(): BlogDto? {
//        return blogDao.getAllBlogPost().let { blogEntity ->
//            entityMapper.mapToDomainModel(blogEntity)
//        }
//    }
//
//
//
//    private suspend fun fetchBlogPost(): BlogResponse {
//        val response = BlogResponse()
//        try {
//            response.Blog = blogRef.get().await().children.map { snapShot ->
//                snapShot.getValue(Blog::class.java)!!
//            }
//        } catch (exception: Exception) {
//            response.exception = exception
//        }
//        return response
//    }
//
//}