package com.kanyandula.malawi.di


import com.kanyandula.malawi.api.BlogApi
import android.app.Application
import androidx.room.Room
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.kanyandula.malawi.api.BlogDtoMapper
import com.kanyandula.malawi.data.BlogDao
import com.kanyandula.malawi.data.BlogDataBase
import com.kanyandula.malawi.data.model.BlogEntityMapper

import com.kanyandula.malawi.utils.Constants.BASE_URL
import com.kanyandula.malawi.utils.Constants.BLOG_REF
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.scopes.ViewModelScoped
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
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
        return  FirebaseDatabase.getInstance().reference.child(BLOG_REF)
        //return FirebaseDatabase.getInstance().getReference(BLOG_REF)
    }



    @Provides
    @Singleton
    fun provideDatabase(app: Application): BlogDataBase =
        Room.databaseBuilder(app, BlogDataBase::class.java, "blog_article_database")
            .fallbackToDestructiveMigration()
            .build()


    @Singleton
    @Provides
    fun provideCacheBlogMapper(): BlogEntityMapper {
        return BlogEntityMapper()
    }

    @Singleton
    @Provides
    fun provideMovieMapper(): BlogDtoMapper {
        return BlogDtoMapper()
    }


//    @ViewModelScoped
//    @Provides
//    fun  provideGetBlogPost(
//        blogRef: DatabaseReference,
//        blogDao: BlogDao,
//        entityMapper: BlogEntityMapper
//    ):GetBlogPost{
//        return  GetBlogPost(
//            blogRef = blogRef,
//            blogDao = blogDao,
//            entityMapper = entityMapper,
//
//        )
//
//    }


    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    @Provides
    fun providesLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
    }

    @Singleton
    @Provides
    fun providesOkhttpClient(
        httpLoggingInterceptor: HttpLoggingInterceptor,
       ) =
        OkHttpClient.Builder()
            .addInterceptor(httpLoggingInterceptor)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()

    @Provides
    @Singleton
    fun provideNewsApi(retrofit: Retrofit): BlogApi =
        retrofit.create(BlogApi::class.java)


}