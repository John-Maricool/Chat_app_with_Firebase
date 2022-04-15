package com.example.firebasechatapp.data.repositories

import android.content.Context
import com.example.firebasechatapp.cache_source.UserDao
import com.example.firebasechatapp.cache_source.UserEntity
import com.example.firebasechatapp.data.mapper.CacheMapperImpl
import com.example.firebasechatapp.data.models.Chat
import com.example.firebasechatapp.data.models.ChatWithUserInfo
import com.example.firebasechatapp.utils.Constants
import com.example.firebasechatapp.utils.Result
import com.example.firebasechatapp.utils.checkForInternet
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class ChatsListRepository
@Inject constructor(
    val cloud: CloudRepository,
    val auth: AuthRepository,
    val dao: UserDao,
    val mapperImpl: CacheMapperImpl,
    @ApplicationContext val context: Context,
) {

    suspend fun getAllChats(): List<UserEntity> {
        return dao.getAllUsers()
    }

    suspend fun getAllChats(b: (Result<List<UserEntity>>) -> Unit) {
        if (!context.checkForInternet()) {
            b.invoke(Result.Error(Constants.check_internet))
        } else {
            b.invoke(Result.Loading)
            try {
                val chatsIds = cloud.getChatsIds(auth.getUserID())
                val sourceChats = mutableListOf<ChatWithUserInfo>()
                chatsIds.forEach { id ->
                    val channelId = cloud.getChatChannel(auth.getUserID(), id)
                    val userInfo = cloud.getUserInfo(id)
                    val lastMessage = cloud.getLastMessage(channelId!!)
                    val chatWithUserInfo = ChatWithUserInfo(
                        mChat = Chat(lastMessage!!, channelId),
                        mUserInfo = userInfo!!
                    )
                    sourceChats.add(chatWithUserInfo)
                }
                val cacheChats = mapperImpl.mapAllToCache(sourceChats)
                dao.deleteAll()
                dao.addAllUsersToDd(cacheChats)
                b.invoke(Result.Success(cacheChats))
            } catch (e: Exception) {
                b.invoke(Result.Error(e.toString()))
            }
        }
    }

    suspend fun getChannelId(otherId: String, b: (Result<String>) -> Unit) {
        b.invoke(Result.Loading)
        try {
            val result = cloud.getChatChannel(auth.getUserID(), otherId)
            b.invoke(Result.Success(result))
        } catch (e: Exception) {
            b.invoke(Result.Error(e.toString()))
        }
    }
}