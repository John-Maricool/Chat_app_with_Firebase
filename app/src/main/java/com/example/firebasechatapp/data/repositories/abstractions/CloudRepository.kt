package com.example.firebasechatapp.data.repositories.abstractions

import com.example.firebasechatapp.data.models.Message
import com.example.firebasechatapp.data.models.UserInfo
import com.example.firebasechatapp.utils.Result

interface CloudRepository {

    suspend fun getChatsIds(): List<String>
    suspend fun getChatChannel(chatId: String): String?
    suspend fun getLastMessage(channelId: String): Message?

    fun getAllMessages(
        channelId: String,
        b: (Result<List<Message>>) -> Unit
    )

    fun reloadNewPageOfMessages(
        channelId: String, b: (Result<List<Message>>) -> Unit
    )

    suspend fun getUserInfo(id: String): UserInfo
    fun getChangedUserInfo(id: String, b: ((Result<UserInfo>) -> Unit))
    suspend fun createChatChannel(
        secUser: String,
    ): String

    suspend fun addUserToChats(otherId: String)
    suspend fun sendMessage(message: Message, channelId: String)
    suspend fun sendMessage(channelId: String, message: Message, b: (Result<String>) -> Unit)
}




