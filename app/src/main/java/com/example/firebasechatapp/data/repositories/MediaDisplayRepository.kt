package com.example.firebasechatapp.data.repositories

import com.example.firebasechatapp.utils.Result
import java.util.*
import javax.inject.Inject

class MediaDisplayRepository
@Inject constructor(
    val cloud: CloudRepository,
    val auth: AuthRepository,
    val storage: StorageRepository
) {

    suspend fun storeMedia(media: String, channelId: String, b: (Result<String>) -> Unit) {
        b.invoke(Result.Loading)
        try {
            val random = UUID.randomUUID()
            storage.putChatMedia("${channelId}/${random}", media)
            val res = storage.getChatMediaDownloadString("${channelId}/${random}")
            b.invoke(Result.Success(res.toString()))
        } catch (e: Exception) {
            b.invoke(Result.Error(e.toString()))
        }
    }
}