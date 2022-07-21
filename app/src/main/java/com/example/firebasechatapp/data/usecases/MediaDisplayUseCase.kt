package com.example.firebasechatapp.data.usecases

import android.content.Context
import android.graphics.Bitmap
import com.example.firebasechatapp.data.models.Message
import com.example.firebasechatapp.data.repositories.abstractions.StorageRepository
import com.example.firebasechatapp.data.repositories.abstractions.CloudRepository
import com.example.firebasechatapp.utils.Result
import com.example.firebasechatapp.utils.SharedPrefsCalls
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.ByteArrayOutputStream
import java.util.*
import javax.inject.Inject


class MediaDisplayUseCase
@Inject constructor(
    val cloud: CloudRepository,
    val storage: StorageRepository,
    val prefs: SharedPrefsCalls,
    @ApplicationContext val context: Context
) {

    val uid = prefs.getUserUid()

    suspend fun storeMedia(media: Bitmap, channelId: String, b: (Result<String>) -> Unit) {
        b.invoke(Result.Loading)
        try {
            val random = UUID.randomUUID()
            val imageBytes = compress(media)
            storage.putChatMedia("${channelId}/${random}", imageBytes)
            val res = storage.getChatMediaDownloadString("${channelId}/${random}")
            b.invoke(Result.Success(res.toString()))
        } catch (e: Exception) {
            b.invoke(Result.Error(e.toString()))
        }
    }

    private fun compress(img: Bitmap): ByteArray {
        val byteArrayOutputStream = ByteArrayOutputStream()
        img.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
        return byteArrayOutputStream.toByteArray()
    }

    suspend fun sendMessage(channelId: String, message: Message, b: (Result<String>) -> Unit) {
        cloud.sendMessage(channelId, message, b)
    }

}