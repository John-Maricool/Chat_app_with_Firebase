package com.example.firebasechatapp.data.repositories

import com.example.firebasechatapp.cache_source.UserDao
import com.example.firebasechatapp.data.mapper.CacheMapperImpl
import com.example.firebasechatapp.data.models.Chat
import com.example.firebasechatapp.data.models.ChatWithUserInfo
import com.example.firebasechatapp.data.models.Message
import com.example.firebasechatapp.utils.Result
import com.example.firebasechatapp.utils.SharedPrefsCalls
import java.util.*
import javax.inject.Inject

class FirstMessageRepository
@Inject constructor(
    val dao: UserDao,
    val mapper: CacheMapperImpl,
    val cloud: CloudRepository,
    val prefs: SharedPrefsCalls
) {

    suspend fun send(secUserId: String, messageText: String, b: (Result<String>) -> Unit) {
        b.invoke(Result.Loading)
        val uid = prefs.getUserUid()
        try {
            val message = Message(uid!!, secUserId, messageText, false, Date().time)
            val id = cloud.createChatChannel(uid, secUserId)
            cloud.addUserToChats(uid, secUserId)
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





