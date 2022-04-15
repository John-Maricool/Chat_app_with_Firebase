package com.example.firebasechatapp.cache_source

import androidx.room.*

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addUserToDb(user: UserEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addAllUsersToDd(users: List<UserEntity>)

    @Query("select * from UserEntity order by sentTime")
    suspend fun getAllUsers(): List<UserEntity>

    @Query("select * from UserEntity where id = :id")
    suspend fun getUser(id: String): UserEntity

    @Query("delete from UserEntity")
    suspend fun deleteAll()
}