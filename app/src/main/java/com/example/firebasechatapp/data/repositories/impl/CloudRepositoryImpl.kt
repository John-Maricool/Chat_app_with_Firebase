package com.example.firebasechatapp.data.repositories.impl

import com.example.firebasechatapp.data.source.remote.FirebaseFirestoreSource
import com.example.firebasechatapp.data.models.Message
import com.example.firebasechatapp.data.models.UserInfo
import com.example.firebasechatapp.data.repositories.abstractions.CloudRepository
import com.example.firebasechatapp.utils.Constants.currentPage
import com.example.firebasechatapp.utils.Result
import com.example.firebasechatapp.utils.SharedPrefsCalls

class CloudRepositoryImpl(var cloudSource: FirebaseFirestoreSource, val prefs: SharedPrefsCalls) :
    CloudRepository {

    fun userID(): String? {
        return prefs.getUserUid()
    }

    override suspend fun getChatsIds(): List<String> {
        val ids = mutableListOf<String>()
        val res = cloudSource.getChats(userID()!!)?.documents
        res?.forEach {
            ids.add(it.reference.id)
        }
        return ids
    }

    override suspend fun getUserInfo(id: String): UserInfo {
        val result = cloudSource.getUserInfo(id).toObject(UserInfo::class.java)
        return result!!
    }

    override fun getChangedUserInfo(id: String, b: ((Result<UserInfo>) -> Unit)) {
        b.invoke(Result.Loading)
        try {
            cloudSource.getChangedUserInfo(id).addSnapshotListener { value, error ->
                if (value != null && error == null) {
                    val result = value.toObject(UserInfo::class.java)
                    b.invoke(Result.Success(result))
                }
            }
        } catch (it: java.lang.Exception) {
            b.invoke(Result.Error(it.toString()))
        }
    }

    override suspend fun getChatChannel(chatId: String): String? {
        return cloudSource.getChatChannelId(userID()!!, chatId)?.get("id")?.toString()
    }

    override suspend fun getLastMessage(channelId: String): Message? {
        return cloudSource.getLastMessageFromChat(channelId)?.toObject(Message::class.java)
    }

    override fun getAllMessages(
        channelId: String,
        b: (Result<List<Message>>) -> Unit
    ) {

        cloudSource.getAllMessages(channelId).addSnapshotListener { value, error ->
            if (value != null && error == null) {
                value.documents.forEach { doc ->
                    if (doc["receiverId"] == prefs.getUserUid() && doc["seen"] == false)
                        doc.reference.update("seen", true)
                }
                b.invoke(
                    Result.Success(
                        value.toObjects(Message::class.java).distinct().reversed()
                    )
                )
            } else {
                b.invoke(Result.Error("Error loading Data"))
            }
        }
    }

/*    override fun reloadNewPageOfMessages(
        channelId: String, b: (Result<List<Message>>) -> Unit
    ) {
        currentPage++
        cloudSource.getReloadedMessages(channelId).get().addOnSuccessListener { value ->
            b.invoke(
                Result.Success(
                    value?.toObjects(Message::class.java)?.distinct()?.reversed()
                )
            )
        }
    }*/


    override suspend fun createChatChannel(
        secUser: String,
    ): String {
        return cloudSource.createChatInfo(userID()!!, secUser)
    }

    override suspend fun addUserToChats(otherId: String) {
        cloudSource.addUserToChats(userID()!!, otherId)
    }

    override suspend fun sendMessage(message: Message, channelId: String) {
        cloudSource.sendMessage(message, channelId)
    }

    override suspend fun sendMessage(
        channelId: String,
        message: Message,
        b: (Result<String>) -> Unit
    ) {
        b.invoke(Result.Loading)
        try {
            cloudSource.sendMessage(message, channelId)
            b.invoke(Result.Success("Successful"))
        } catch (e: Exception) {
            b.invoke(Result.Error(e.toString()))
        }
    }
}




