package com.example.firebasechatapp.data.db.remote

import android.net.Uri
import androidx.core.net.toUri
import com.google.android.gms.tasks.Task
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirebaseStorageSource
@Inject constructor(val source: FirebaseStorage){

    suspend fun putFileInUserStorage(userID: String, file: String): UploadTask.TaskSnapshot? {
        val ref = source.reference.child("images/$userID")
        return ref.putFile(file.toUri()).await()
    }

    suspend fun getDownloadUriOfUserImage(userID: String): Uri? {
        val ref = source.reference.child("images/$userID")
        return ref.downloadUrl.await()
    }

    suspend fun putMediaInChatStorage(channelId: String, media: String): UploadTask.TaskSnapshot? {
        val ref  = source.reference.child("media/$channelId")
        return ref.putFile(media.toUri()).await()
    }

    suspend fun getDownloadUriOfChatMedia(channelId: String): Uri? {
        val ref = source.reference.child("media/$channelId")
        return ref.downloadUrl.await()
    }
}