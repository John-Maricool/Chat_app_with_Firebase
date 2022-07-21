package com.example.firebasechatapp.data.repositories.abstractions

import com.example.firebasechatapp.data.models.UserInfo
import com.example.firebasechatapp.utils.Result

/**
 * This repo is just for user database related issues.
 * Creating and updating single user info
 */
interface RemoteUserRepository {
    suspend fun getUserInfo(): UserInfo?
    suspend fun toggleOnline(online: Boolean)
    fun changeUserName(newName: String, b: (Result<String>) -> Unit)
    suspend fun uploadUserData(userInfo: UserInfo)
}

