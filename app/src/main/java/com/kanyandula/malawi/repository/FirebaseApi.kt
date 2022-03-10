package com.kanyandula.malawi.repository



import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.kanyandula.malawi.api.BlogDto
import com.kanyandula.malawi.api.BlogResponse
import com.kanyandula.malawi.data.Blog
import com.kanyandula.malawi.di.AppModule
import com.kanyandula.malawi.utils.Resource
import dagger.Component
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Singleton


interface FirebaseApi {


         var blogRef: DatabaseReference





}