package com.example.firebasechatapp.data.repositories.abstractions

import androidx.lifecycle.LiveData
import com.example.firebasechatapp.data.models.Message
import com.example.firebasechatapp.data.models.UserInfo
import com.example.firebasechatapp.utils.Result

interface CloudRepository {

    suspend fun getChatsIds(id: String): List<String>
    suspend fun getChatChannel(userId: String, chatId: String): String?
    suspend fun getLastMessage(channelId: String): Message?

    fun getAllMessages(
        channelId: String,
        b: (Result<List<Message>>) -> Unit
    )

    fun reloadNewPageOfMessages(
        channelId: String, b: (Result<List<Message>>) -> Unit
    )

    suspend fun getUserInfo(id: String, b: ((Result<UserInfo>) -> Unit))

    fun getChangedUserInfo(id: String, b: ((Result<UserInfo>) -> Unit))
    suspend fun getUserInfo(id: String): UserInfo?
    suspend fun toggleOnline(id: String, online: Boolean)
    suspend fun createChatChannel(
        user: String,
        secUser: String,
    ): String

    suspend fun addUserToChats(userId: String, otherId: String)
    suspend fun sendMessage(message: Message, channelId: String)
    suspend fun sendMessage(channelId: String, message: Message, b: (Result<String>) -> Unit)
    fun changeUserName(name: String, b: (Result<String>) -> Unit)
    suspend fun checkIfUserHasNewMessages(userId: String): LiveData<Boolean>
}




