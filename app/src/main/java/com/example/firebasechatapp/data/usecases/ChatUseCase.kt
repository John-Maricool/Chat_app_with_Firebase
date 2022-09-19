package com.example.firebasechatapp.data.usecases

import com.example.firebasechatapp.data.models.Message
import com.example.firebasechatapp.data.models.UserInfo
import com.example.firebasechatapp.data.repositories.abstractions.CloudRepository
import com.example.firebasechatapp.data.repositories.abstractions.RemoteUserRepository
import com.example.firebasechatapp.utils.Result
import javax.inject.Inject

class ChatUseCase
@Inject constructor(var cloud: CloudRepository, val user: RemoteUserRepository) {

    fun getAllMessages(channelId: String, b: (Result<List<Message>>) -> Unit) {
        cloud.getAllMessages(channelId) {
            b.invoke(it)
        }
    }

   /* fun loadOldMessages(channelId: String, b: (Result<List<Message>>) -> Unit) {
        cloud.reloadNewPageOfMessages(channelId) {
            b.invoke(it)
        }
    }*/

    fun getUserInfo(otherUserId: String, b: (Result<UserInfo>) -> Unit) {
        cloud.getChangedUserInfo(otherUserId) {
            b.invoke(it)
        }
    }

    suspend fun sendMessage(channelId: String, message: Message, b: (Result<String>) -> Unit) {
        cloud.sendMessage(channelId, message) {
            b.invoke(it)
        }
    }


}