package com.example.firebasechatapp.data.repositories

import com.example.firebasechatapp.data.db.remote.FirebaseFirestoreSource
import com.example.firebasechatapp.data.models.*
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

    fun getChatsWithUserInfo(id: String, b: ((Result<List<ChatWithUserInfo>>) -> Unit)) {
        b.invoke(Result.Loading)
        val cUserInfo = mutableListOf<ChatWithUserInfo>()
        cloudSource.getChats(id).addOnSuccessListener { snap ->
            snap.documents.forEach {
                getUserInfo(it.reference.id) { result ->
                    if (result is Result.Success) {
                        getChat(id, result.data!!.id) { res ->
                            if (res is Result.Success) {
                                val chatWithUserInfo = ChatWithUserInfo(res.data!!, result.data)
                                cUserInfo.add(chatWithUserInfo)
                            }
                        }
                    }
                }
            }
            b.invoke(Result.Success(cUserInfo))
        }.addOnFailureListener {
            b.invoke(Result.Error(it.toString()))
        }
    }

    private fun getChat(userId: String, chatId: String, b: (Result<Chat>) -> Unit) {
        b.invoke(Result.Loading)
        cloudSource.getChatChannelId(userId, chatId).addOnSuccessListener {
            val res = it.toObject(ChatInfo::class.java)
            if (res != null) {
                getLastMessage(res.id) { resMes ->
                    if (resMes is Result.Success) {
                        val chat = Chat(resMes.data!!, res.id)
                        b.invoke(Result.Success(chat))
                    } else {
                        b.invoke(Result.Error("Error"))
                    }
                }
            } else {
                b.invoke(Result.Error("Error"))
            }
        }.addOnFailureListener {
            b.invoke(Result.Error(it.toString()))
        }
    }

    private fun getLastMessage(channelId: String, b: (Result<Message>) -> Unit) {
        b.invoke(Result.Loading)
        cloudSource.getLastMessageFromChat(channelId).get().addOnSuccessListener {
            val res = it.toObject(Message::class.java)
            if (res != null) {
                b.invoke(Result.Success(res))
            }
        }.addOnFailureListener {
            b.invoke(Result.Error(it.toString()))
        }
    }

    fun getAllMessages(userId: String, channelId: String, b: (Result<List<Message>>) -> Unit) {
        b.invoke(Result.Loading)
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

    fun getChatChannel(userId: String, otherId: String, b: (Result<String>) -> Unit) {
        b.invoke(Result.Loading)
        cloudSource.getChatChannelId(userId, otherId).addOnSuccessListener {
            b.invoke(Result.Success(it["id"].toString()))
        }.addOnFailureListener {
            b.invoke(Result.Error(it.toString()))
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

    fun getUserInfo(id: String, b: ((Result<UserInfo>) -> Unit)) {
        b.invoke(Result.Loading)
        cloudSource.getUserInfo(id).addOnSuccessListener { res ->
            val result = res.toObject(UserInfo::class.java)
            b.invoke(Result.Success(result))
        }.addOnFailureListener {
            b.invoke(Result.Error(it.toString()))
        }
    }

    fun toggleOnline(id: String, online: Boolean, b: ((Result<Boolean>) -> Unit)) {
        cloudSource.toggleOnline(id, online).addOnSuccessListener {
            b.invoke(Result.Success(true))
        }.addOnFailureListener {
            b.invoke(Result.Error(it.toString()))
        }
    }

    fun saveUserDetailsToDb(userInfo: UserInfo, b: ((Result<Boolean>) -> Unit)) {
        b.invoke(Result.Loading)
        cloudSource.saveUserDetailsToDb(userInfo).addOnSuccessListener {
            b.invoke(Result.Success(true))
        }.addOnFailureListener {
            b.invoke(Result.Error(it.toString()))
        }
    }

    suspend fun createChatInfoWithFirstMessage(
        user: String,
        secUser: String,
        message: Message,
        b: ((Result<String>) -> Unit)
    ) {
        b.invoke(Result.Loading)
        try {
            val id = cloudSource.createChatInfo(user, secUser)
            cloudSource.sendMessage(message, id)
            cloudSource.addUserToChats(user, secUser)
            b.invoke(Result.Success(id))
        } catch (e: Exception) {
            b.invoke(Result.Error(e.toString()))
        }
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