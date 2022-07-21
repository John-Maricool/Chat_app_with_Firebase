package com.example.firebasechatapp.data.usecases

import com.example.firebasechatapp.data.source.local.UserDao
import com.example.firebasechatapp.data.mapper.CacheMapperImpl
import com.example.firebasechatapp.data.models.Chat
import com.example.firebasechatapp.data.models.ChatWithUserInfo
import com.example.firebasechatapp.data.models.Message
import com.example.firebasechatapp.data.repositories.abstractions.CloudRepository
import com.example.firebasechatapp.data.repositories.impl.CloudRepositoryImpl
import com.example.firebasechatapp.utils.Result
import java.util.*
import javax.inject.Inject

class FirstMessageUseCase
@Inject constructor(
    val dao: UserDao,
    val mapper: CacheMapperImpl,
    val cloud: CloudRepository
) {

    suspend fun send(secUserId: String, messageText: String, b: (Result<String>) -> Unit) {
        b.invoke(Result.Loading)
        val uid = (cloud as CloudRepositoryImpl).userID()
        try {
            val message = Message(uid!!, secUserId, messageText, false, Date().time)
            val id = cloud.createChatChannel(secUserId)
            cloud.addUserToChats(secUserId)
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





