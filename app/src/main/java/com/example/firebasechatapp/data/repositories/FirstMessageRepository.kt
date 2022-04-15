package com.example.firebasechatapp.data.repositories

import com.example.firebasechatapp.cache_source.UserDao
import com.example.firebasechatapp.data.mapper.CacheMapperImpl
import com.example.firebasechatapp.data.models.Message
import com.example.firebasechatapp.data.models.Chat
import com.example.firebasechatapp.data.models.ChatWithUserInfo
import com.example.firebasechatapp.utils.Result
import javax.inject.Inject

class FirstMessageRepository
@Inject constructor(
    val dao: UserDao,
    val mapper: CacheMapperImpl,
    val cloud: CloudRepository,
    val auth: AuthRepository
) {

    suspend fun send(secUserId: String, message: Message, b: (Result<String>) -> Unit) {
        b.invoke(Result.Loading)
        try {
            val id = cloud.createChatChannel(auth.getUserID(), secUserId)
            cloud.addUserToChats(auth.getUserID(), secUserId)
            val userInfo = cloud.getUserInfo(secUserId)!!
            cloud.sendMessage(message, id)
            val chatWithUserInfo = ChatWithUserInfo(Chat(message, id), userInfo)
            val userEntity = mapper.mapToCache(chatWithUserInfo)
            dao.addUserToDb(userEntity)
            b.invoke(Result.Success(id))
        } catch (e: Exception) {
            b.invoke(Result.Error(e.toString()))
        }
    }
}