package com.example.firebasechatapp.data.repositories

import android.net.Uri

interface StorageRepository {

    suspend fun putUserImage(userId: String, image: String)

    suspend fun getUserImageDownloadString(userId: String): Uri?

    suspend fun putChatMedia(channelId: String, media: String)

    suspend fun getChatMediaDownloadString(channelId: String): Uri?
}