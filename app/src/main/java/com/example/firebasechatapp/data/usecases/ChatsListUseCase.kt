package com.example.firebasechatapp.data.usecases

import android.util.Log
import androidx.lifecycle.LiveData
import com.example.firebasechatapp.data.source.local.UserDao
import com.example.firebasechatapp.data.source.local.UserEntity
import com.example.firebasechatapp.data.mapper.CacheMapperImpl
import com.example.firebasechatapp.data.models.Chat
import com.example.firebasechatapp.data.models.ChatWithUserInfo
import com.example.firebasechatapp.data.repositories.abstractions.CloudRepository
import com.example.firebasechatapp.data.repositories.abstractions.RemoteUserRepository
import com.example.firebasechatapp.utils.Result
import javax.inject.Inject

class ChatsListUseCase
@Inject constructor(
    private val cloud: CloudRepository,
    private val remote: RemoteUserRepository,
    private val dao: UserDao,
    private val mapperImpl: CacheMapperImpl
) {

    fun getAllChats(): LiveData<List<UserEntity>> {
        return dao.getAllUsers()
    }

    suspend fun cacheChatList(b: (Result<LiveData<List<UserEntity>>>) -> Unit) {
        b.invoke(Result.Loading)
        try {
            val chatsIds = cloud.getChatsIds()
            val sourceChats = mutableListOf<ChatWithUserInfo>()
            chatsIds.forEach { id ->
                val channelId = cloud.getChatChannel(id)
                val userInfo = cloud.getUserInfo(id)
                val lastMessage = cloud.getLastMessage(channelId!!)
                val chatWithUserInfo = ChatWithUserInfo(
                    mChat = Chat(lastMessage!!, channelId),
                    mUserInfo = userInfo
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
            val result = cloud.getChatChannel(otherId)
            b.invoke(Result.Success(result))
        } catch (e: Exception) {
            b.invoke(Result.Error(e.toString()))
        }
    }
}