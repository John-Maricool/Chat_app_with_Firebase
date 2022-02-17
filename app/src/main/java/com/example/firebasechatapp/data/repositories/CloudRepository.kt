package com.example.firebasechatapp.data.repositories

import com.example.firebasechatapp.data.db.remote.FirebaseFirestoreSource
import com.example.firebasechatapp.data.models.Message
import com.example.firebasechatapp.data.models.UserInfo
import com.example.firebasechatapp.utils.Result
import javax.inject.Inject

class CloudRepository
@Inject constructor(var cloudSource: FirebaseFirestoreSource){

    fun uploadUsers(userInfo: UserInfo, b: ((Result<Unit>) -> Unit) ){
        cloudSource.uploadUserDetailsToDb(userInfo).addOnSuccessListener {
            b.invoke(Result.Success(Unit))
        }.addOnFailureListener {
            b.invoke(Result.Error(it.toString()))
        }
    }

    fun getChats(id: String, b: ((Result<List<UserInfo>>) -> Unit)){
        val userInfo = mutableListOf<UserInfo>()
        cloudSource.getChats(id).addOnSuccessListener { querySnapshot ->
            querySnapshot.documents.forEach {
                    getUserInfo(it.reference.id){result ->
                        if (result is Result.Success)
                            userInfo.add(result.data!!)
                        b.invoke(Result.Success(userInfo))
                    }
                }
        }.addOnFailureListener {
            b.invoke(Result.Error(it.toString()))
        }
    }

    fun getUserInfo(id: String, b: ((Result<UserInfo>) -> Unit)){
        cloudSource.getUserInfo(id).addOnSuccessListener {
            b.invoke(Result.Success(it.toObject(UserInfo::class.java)) as Result<UserInfo>)
        }.addOnFailureListener {
            b.invoke(Result.Error(it.toString()))
        }
    }

    fun getLastMessage(id: String, chatId: String, b: ((Result<Message>) -> Unit)){
        cloudSource.getLastMessageFromChat(id, chatId).addOnSuccessListener {
            b.invoke(Result.Success(it.toObject(Message::class.java)) as Result<Message>)
        }.addOnFailureListener {
            b.invoke(Result.Error(it.toString()))
        }
    }

    fun toggleOnline(id: String,  online: Boolean, b: ((Result<Boolean>) -> Unit)) {
        cloudSource.toggleOnline(id, online).addOnSuccessListener {
            b.invoke(Result.Success(true))
        }.addOnFailureListener {
            b.invoke(Result.Error(it.toString()))
        }
    }
}