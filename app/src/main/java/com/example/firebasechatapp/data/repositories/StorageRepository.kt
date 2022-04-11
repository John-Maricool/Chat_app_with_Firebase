package com.example.firebasechatapp.data.repositories

import com.example.firebasechatapp.data.db.remote.FirebaseStorageSource
import com.example.firebasechatapp.utils.Result
import javax.inject.Inject

class StorageRepository
@Inject constructor(val storage: FirebaseStorageSource) {

    fun putUserImage(userId: String, image: String, b: (Result<String>) -> Unit) {
        b.invoke(Result.Loading)
        storage.putFileInUserStorage(userId, image).addOnSuccessListener {
            b.invoke(Result.Success("Successful"))
        }.addOnFailureListener {
            b.invoke(Result.Error(it.toString()))
        }
    }

    fun getUserImageDownloadString(userId: String, image: String, b: (Result<String>) -> Unit) {
        b.invoke(Result.Loading)
        storage.getDownloadUriOfUserImage(userId, image).addOnSuccessListener {
            b.invoke(Result.Success(it.toString()))
        }.addOnFailureListener {
            b.invoke(Result.Error(it.toString()))
        }
    }

    fun putChatMedia(channelId: String, media: String, b: (Result<String>) -> Unit) {
        b.invoke(Result.Loading)
        storage.putMediaInChatStorage(channelId, media).addOnSuccessListener {
            b.invoke(Result.Success("Successful"))
        }.addOnFailureListener {
            b.invoke(Result.Error(it.toString()))
        }
    }

    fun getChatMediaDownloadString(channelId: String, media: String, b: (Result<String>) -> Unit) {
        b.invoke(Result.Loading)
        storage.getDownloadUriOfChatMedia(channelId, media).addOnSuccessListener {
            b.invoke(Result.Success(it.toString()))
        }.addOnFailureListener {
            b.invoke(Result.Error(it.toString()))
        }
    }
}