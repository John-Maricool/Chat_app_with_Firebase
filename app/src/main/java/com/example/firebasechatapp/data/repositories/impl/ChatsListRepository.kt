package com.example.firebasechatapp.data.repositories.impl

import android.util.Log
import com.example.firebasechatapp.cache_source.UserDao
import com.example.firebasechatapp.cache_source.UserEntity
import com.example.firebasechatapp.data.mapper.CacheMapperImpl
import com.example.firebasechatapp.data.models.Chat
import com.example.firebasechatapp.data.models.ChatWithUserInfo
import com.example.firebasechatapp.data.repositories.abstractions.CloudRepository
import com.example.firebasechatapp.utils.Result
import com.example.firebasechatapp.utils.SharedPrefsCalls
import javax.inject.Inject

class ChatsListRepository
@Inject constructor(
    private val cloud: CloudRepository,
    private val prefs: SharedPrefsCalls,
    private val dao: UserDao,
    private val mapperImpl: CacheMapperImpl
) {

    suspend fun getAllChats(): List<UserEntity> {
        return dao.getAllUsers()
    }

    suspend fun cacheChatList(b: (Result<List<UserEntity>>) -> Unit) {
        b.invoke(Result.Loading)
        try {
            val chatsIds = cloud.getChatsIds(prefs.getUserUid()!!)
            val sourceChats = mutableListOf<ChatWithUserInfo>()
            chatsIds.forEach { id ->
                val channelId = cloud.getChatChannel(prefs.getUserUid()!!, id)
                val userInfo = cloud.getUserInfo(id)
                val lastMessage = cloud.getLastMessage(channelId!!)
                val chatWithUserInfo = ChatWithUserInfo(
                    mChat = Chat(lastMessage!!, channelId),
                    mUserInfo = userInfo!!
                )
                sourceChats.add(chatWithUserInfo)
            }
            val cacheChats = mapperImpl.mapAllToCache(sourceChats)
            Log.d("chats", cacheChats.toString())
            dao.deleteAll()
            dao.addAllUsersToDd(cacheChats)
            b.invoke(Result.Success(dao.getAllUsers()))
        } catch (e: Exception) {
            Log.d("error", e.toString())
            b.invoke(Result.Error(e.toString()))
        }
    }

    suspend fun getChannelId(otherId: String, b: (Result<String>) -> Unit) {
        b.invoke(Result.Loading)
        try {
            val result = cloud.getChatChannel(prefs.getUserUid()!!, otherId)
            b.invoke(Result.Success(result))
        } catch (e: Exception) {
            b.invoke(Result.Error(e.toString()))
        }
    }
}