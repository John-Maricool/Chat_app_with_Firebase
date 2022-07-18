package com.example.firebasechatapp.data.repositories

import android.net.Uri
import com.example.firebasechatapp.data.db.remote.FirebaseStorageSource

class StorageRepositoryImpl(val storage: FirebaseStorageSource) : StorageRepository {

    override suspend fun putUserImage(userId: String, image: String) {
        storage.putFileInUserStorage(userId, image)
    }

    override suspend fun getUserImageDownloadString(
        userId: String
    ): Uri? {
        return storage.getDownloadUriOfUserImage(userId)
    }

    override suspend fun putChatMedia(channelId: String, media: String) {
        storage.putMediaInChatStorage(channelId, media)
    }

    override suspend fun getChatMediaDownloadString(channelId: String): Uri? {
        return storage.getDownloadUriOfChatMedia(channelId)
    }
}