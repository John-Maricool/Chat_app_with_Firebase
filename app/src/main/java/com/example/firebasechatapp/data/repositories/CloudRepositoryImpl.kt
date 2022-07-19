package com.example.firebasechatapp.data.repositories

import androidx.lifecycle.LiveData
import com.example.firebasechatapp.data.db.remote.FirebaseFirestoreSource
import com.example.firebasechatapp.data.models.Message
import com.example.firebasechatapp.data.models.UserInfo
import com.example.firebasechatapp.utils.Constants.currentPage
import com.example.firebasechatapp.utils.Result

class CloudRepositoryImpl(var cloudSource: FirebaseFirestoreSource) : CloudRepository {

    override suspend fun getChatsIds(id: String): List<String> {
        val ids = mutableListOf<String>()
        val res = cloudSource.getChats(id)?.documents
        res?.forEach {
            ids.add(it.reference.id)
        }
        return ids
    }

    override suspend fun getChatChannel(userId: String, chatId: String): String? {
        return cloudSource.getChatChannelId(userId, chatId)?.get("id")?.toString()
    }

    override suspend fun getLastMessage(channelId: String): Message? {
        return cloudSource.getLastMessageFromChat(channelId)?.toObject(Message::class.java)
    }

    override fun getAllMessages(
        userId: String,
        channelId: String,
        b: (Result<List<Message>>) -> Unit
    ) {

        cloudSource.getAllMessages(channelId).addSnapshotListener { value, error ->
            if (value != null && error == null) {
                value.documents.forEach { doc ->
                    if (doc["receiverId"] == userId && doc["seen"] == false)
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

    override fun reloadNewPageOfMessages(
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
    }

    override suspend fun getUserInfo(id: String, b: ((Result<UserInfo>) -> Unit)) {
        b.invoke(Result.Loading)
        try {
            val result = cloudSource.getUserInfo(id).toObject(UserInfo::class.java)
            b.invoke(Result.Success(result))
        } catch (it: java.lang.Exception) {
            b.invoke(Result.Error(it.toString()))
        }
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

    override suspend fun getUserInfo(id: String): UserInfo? {
        return cloudSource.getUserInfo(id).toObject(UserInfo::class.java)
    }

    override suspend fun toggleOnline(id: String, online: Boolean) {
        cloudSource.toggleOnline(id, online)
    }

    override suspend fun createChatChannel(
        user: String,
        secUser: String,
    ): String {
        return cloudSource.createChatInfo(user, secUser)
    }

    override suspend fun addUserToChats(userId: String, otherId: String) {
        cloudSource.addUserToChats(userId, otherId)
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

    override fun changeUserName(userId: String, name: String, b: (Result<String>) -> Unit) {
        b.invoke(Result.Loading)
        cloudSource.changeName(userId, name).addOnSuccessListener {
            b.invoke(Result.Success("Successful"))
        }.addOnFailureListener {
            b.invoke(Result.Error(it.toString()))
        }
    }

    override fun checkIfUserHasNewMessages(userId: String): LiveData<Boolean> {
        return cloudSource.checkIfUserHasNewMessages(userId)
    }
}




