package com.example.firebasechatapp.data.repositories

import com.example.firebasechatapp.data.db.remote.FirebaseFirestoreSource
import com.example.firebasechatapp.data.models.Message
import com.example.firebasechatapp.data.models.UserInfo
import com.example.firebasechatapp.utils.Result
import javax.inject.Inject

class CloudRepository
@Inject constructor(var cloudSource: FirebaseFirestoreSource) {

    fun uploadUsers(userInfo: UserInfo, b: ((Result<Unit>) -> Unit)) {
        cloudSource.uploadUserDetailsToDb(userInfo).addOnSuccessListener {
            b.invoke(Result.Success(Unit))
        }.addOnFailureListener {
            b.invoke(Result.Error(it.toString()))
        }
    }

    suspend fun getChatsIds(id: String): List<String> {
        val ids = mutableListOf<String>()
        val res = cloudSource.getChats(id)?.documents
        res?.forEach {
            ids.add(it.reference.id)
        }
        return ids
    }

    suspend fun getChatChannel(userId: String, chatId: String): String? {
        return cloudSource.getChatChannelId(userId, chatId)?.get("id")?.toString()
    }

    suspend fun getLastMessage(channelId: String): Message? {
        val res = cloudSource.getLastMessageFromChat(channelId)?.toObject(Message::class.java)
        return res
    }

    fun getAllMessages(userId: String, channelId: String, b: (Result<List<Message>>) -> Unit) {
        cloudSource.getAllMessages(channelId).addSnapshotListener { value, error ->
            if (error == null && value != null) {
                value.documents.forEach { doc ->
                    if (doc["receiverId"] == userId && doc["seen"] == false)
                        doc.reference.update("seen", true)
                }
                b.invoke(Result.Success(value.toObjects(Message::class.java).distinct()))
            } else {
                b.invoke(Result.Error(error.toString()))
            }
        }
    }

    fun getAllUsers(userId: String, b: (Result<List<UserInfo>>) -> Unit) {
        b.invoke(Result.Loading)
        cloudSource.getAllUsers(userId).addOnSuccessListener {
            val user = it.toObjects(UserInfo::class.java)
            b.invoke(Result.Success(user))
        }.addOnFailureListener {
            b.invoke(Result.Error(it.toString()))
        }
    }

    suspend fun getUserInfo(id: String, b: ((Result<UserInfo>) -> Unit)) {
        b.invoke(Result.Loading)
        try {
            val result = cloudSource.getUserInfo(id)?.toObject(UserInfo::class.java)
            b.invoke(Result.Success(result))
        } catch (it: java.lang.Exception) {
            b.invoke(Result.Error(it.toString()))
        }
    }

    fun getChangedUserInfo(id: String, b: ((Result<UserInfo>) -> Unit)) {
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

    suspend fun getUserInfo(id: String): UserInfo? {
        return cloudSource.getUserInfo(id)?.toObject(UserInfo::class.java)
    }

    suspend fun toggleOnline(id: String, online: Boolean) {
        cloudSource.toggleOnline(id, online)
    }

    fun saveUserDetailsToDb(userInfo: UserInfo, b: ((Result<Boolean>) -> Unit)) {
        b.invoke(Result.Loading)
        cloudSource.saveUserDetailsToDb(userInfo).addOnSuccessListener {
            b.invoke(Result.Success(true))
        }.addOnFailureListener {
            b.invoke(Result.Error(it.toString()))
        }
    }

    suspend fun createChatChannel(
        user: String,
        secUser: String,
    ): String {
        return cloudSource.createChatInfo(user, secUser)
    }

    suspend fun addUserToChats(userId: String, otherId: String) {
        cloudSource.addUserToChats(userId, otherId)
    }

    suspend fun sendMessage(message: Message, channelId: String) {
        cloudSource.sendMessage(message, channelId)
    }

    suspend fun isUserAddedToChats(
        user: String,
        secUser: String,
        b: (Result<Boolean>) -> Unit
    ) {
        b.invoke(Result.Loading)
        try {
            val result = cloudSource.isUserAddedToChats(user, secUser)
            if (result != null && result.exists()) {
                b.invoke(Result.Success(true))
            } else {
                b.invoke(Result.Success(false))
            }
        } catch (e: Exception) {
            b.invoke(Result.Error(e.toString()))
        }
    }

    suspend fun sendMessage(channelId: String, message: Message, b: (Result<String>) -> Unit) {
        b.invoke(Result.Loading)
        try {
            cloudSource.sendMessage(message, channelId)
            b.invoke(Result.Success("Successful"))
        } catch (e: Exception) {
            b.invoke(Result.Error(e.toString()))
        }
    }

    fun changeUserName(userId: String, name: String, b: (Result<String>) -> Unit) {
        b.invoke(Result.Loading)
        cloudSource.changeName(userId, name).addOnSuccessListener {
            b.invoke(Result.Success("Successful"))
        }.addOnFailureListener {
            b.invoke(Result.Error(it.toString()))
        }
    }
}