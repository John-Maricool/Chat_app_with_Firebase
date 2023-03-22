package com.example.firebasechatapp.data.source.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addUserToDb(user: UserEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addAllUsersToDd(users: List<UserEntity>)

    @Query("select * from UserEntity order by sentTime desc")
    fun getAllUsers(): LiveData<List<UserEntity>>

    @Query("select * from UserEntity where id = :id")
    suspend fun getUser(id: String): UserEntity

    @Query("delete from UserEntity")
    suspend fun deleteAll()
}