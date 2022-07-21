package com.example.firebasechatapp.data.source.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [UserEntity::class], version = 1, exportSchema = false)
abstract class CacheDatabase: RoomDatabase() {
    abstract fun userDao(): UserDao
}