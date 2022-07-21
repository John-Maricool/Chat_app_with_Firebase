package com.example.firebasechatapp.data.repositories.abstractions

import android.net.Uri
import com.google.android.gms.tasks.Task
import com.google.firebase.storage.ListResult

interface StorageRepository {

    suspend fun putUserImage(userId: String, image: String)

    suspend fun getUserImageDownloadString(userId: String): Uri?

    suspend fun putChatMedia(channelId: String, media: ByteArray)

    suspend fun getChatMediaDownloadString(channelId: String): Uri?

    suspend fun getAllImages(channelId: String): Task<ListResult>

}