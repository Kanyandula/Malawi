package com.kanyandula.malawi.di


import android.app.Application
import androidx.room.Room

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.kanyandula.malawi.data.BlogDataBase
import com.kanyandula.malawi.utils.Constants.BLOG_REF
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Qualifier
import javax.inject.Singleton

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class BlogReference

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideFirebaseDatabaseInstance(): FirebaseDatabase{
        return  FirebaseDatabase.getInstance()
    }

//    @Provides
//   @Singleton
//    fun provideFireBaseDatabase ( firebaseDatabase: FirebaseDatabase ): BlogApi =
//        firebaseDatabase.reference(BlogApi::class.java)



//    @Singleton
//    @Provides
//    fun provideBlogReference (blogRef: FirebaseDatabase): DatabaseReference =
//        blogRef.getReference(BLOG_REF)


    @Singleton
    @Provides
    fun provideBlogReference(blogRef: FirebaseDatabase): DatabaseReference {
        return  blogRef.reference.child(BLOG_REF)
    }

    @Provides
    @Singleton
    fun provideDatabase(app: Application): BlogDataBase =
        Room.databaseBuilder(app, BlogDataBase::class.java, "blog_article_database")
            .fallbackToDestructiveMigration()
            .build()



}