package com.example.firebasechatapp.data.repositories.impl

import android.net.Uri
import com.example.firebasechatapp.data.source.remote.FirebaseStorageSource
import com.example.firebasechatapp.data.repositories.abstractions.StorageRepository
import com.example.firebasechatapp.utils.Result
import com.google.android.gms.tasks.Task
import com.google.firebase.storage.ListResult
import kotlinx.coroutines.tasks.await

class StorageRepositoryImpl(val storage: FirebaseStorageSource) : StorageRepository {

    override suspend fun putUserImage(userId: String, image: String) {
        storage.putFileInUserStorage(userId, image)
    }

    override suspend fun getUserImageDownloadString(
        userId: String
    ): Uri? {
        return storage.getDownloadUriOfUserImage(userId)
    }

    override suspend fun putChatMedia(channelId: String, media: ByteArray) {
        storage.putMediaInChatStorage(channelId, media)
    }

    override suspend fun getChatMediaDownloadString(channelId: String): Uri? {
        return storage.getDownloadUriOfChatMedia(channelId)
    }

    override suspend fun getAllImages(channelId: String): Task<ListResult> {
        return storage.getAllImages(channelId)
    }

    suspend fun getAllImages(channelId: String, b: (Result<List<String>>) -> Unit) {
        b.invoke(Result.Loading)
        val images = mutableListOf<String>()
        try {
            getAllImages(channelId).await().items.forEach {
                images.add(it.downloadUrl.await().toString())
            }
            b.invoke(Result.Success(images))
        } catch (e: Exception) {
            b.invoke(Result.Error(e.toString()))
        }
    }
}