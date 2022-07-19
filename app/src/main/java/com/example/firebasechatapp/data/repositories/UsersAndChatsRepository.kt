package com.example.firebasechatapp.data.repositories

import com.example.firebasechatapp.data.models.UserInfo
import com.example.firebasechatapp.utils.Result

interface UsersAndChatsRepository {
    fun getList(b: (Result<List<UserInfo>>) -> Unit)
}