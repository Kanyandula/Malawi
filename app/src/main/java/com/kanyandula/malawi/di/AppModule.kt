package com.kanyandula.malawi.di


import android.app.Application
import androidx.room.Room
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.kanyandula.malawi.utils.Constants.BLOG_REF
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton



@Module
@InstallIn(SingletonComponent::class)
object AppModule {


    @Singleton
    @Provides
    fun provideFirebaseAuth(): FirebaseAuth {
        return Firebase.auth
    }


    @Singleton
    @Provides
    fun provideFirebaseReference(): DatabaseReference{
       // return  FirebaseDatabase.getInstance().reference.child(BLOG_REF)
        return FirebaseDatabase.getInstance().getReference(BLOG_REF)
    }







//    @Provides
//    @Singleton
//    fun provideDatabase(app: Application): BlogDataBase =
//        Room.databaseBuilder(app, BlogDataBase::class.java, "blog_article_database")
//            .fallbackToDestructiveMigration()
//            .build()



}