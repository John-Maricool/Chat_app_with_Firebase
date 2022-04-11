package com.example.firebasechatapp.data.db.remote

import android.net.Uri
import androidx.core.net.toUri
import com.google.android.gms.tasks.Task
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import javax.inject.Inject

class FirebaseStorageSource
@Inject constructor(val source: FirebaseStorage){

    fun putFileInUserStorage(userID: String, file: String): UploadTask {
        val ref = source.reference.child("images/$userID")
        return ref.putFile(file.toUri())
    }

    fun getDownloadUriOfUserImage(userID: String, file: String): Task<Uri> {
        val ref = source.reference.child("images/$userID")
        return ref.downloadUrl
    }

    fun putMediaInChatStorage(channelId: String, media: String): UploadTask {
        val ref  = source.reference.child("$channelId/media")
        return ref.putFile(media.toUri())
    }

    fun getDownloadUriOfChatMedia(channelId: String, media: String): Task<Uri> {
        val ref = source.reference.child("$channelId/media")
        return ref.downloadUrl
    }
}