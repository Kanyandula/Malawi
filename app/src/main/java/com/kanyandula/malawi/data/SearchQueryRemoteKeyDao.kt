//package com.kanyandula.malawi.data
//
//import androidx.room.Dao
//import androidx.room.Insert
//import androidx.room.OnConflictStrategy
//import androidx.room.Query
//import com.kanyandula.malawi.data.SearchQueryRemoteKey
//
//@Dao
//interface SearchQueryRemoteKeyDao {
//
//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    suspend fun insertRemoteKey(remoteKey: SearchQueryRemoteKey)
//
//    @Query("SELECT * FROM search_query_remote_keys WHERE searchQuery = :searchQuery")
//    suspend fun getRemoteKey(searchQuery: String): SearchQueryRemoteKey
//}