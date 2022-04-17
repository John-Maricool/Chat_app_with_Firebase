package com.example.firebasechatapp.ui.mediaDisplay

import android.app.DownloadManager
import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.firebasechatapp.data.repositories.AuthRepository
import com.example.firebasechatapp.data.repositories.CloudRepository
import com.example.firebasechatapp.data.repositories.DefaultRepository
import com.example.firebasechatapp.utils.Constants
import com.example.firebasechatapp.utils.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

@HiltViewModel
class MediaViewModel
@Inject constructor(
    @ApplicationContext val application: Context,
    val cloud: CloudRepository,
    val defaultRepo: DefaultRepository,
    val auth: AuthRepository,
) : ViewModel() {

    var type: Int? = null
    var media: String? = null
    var channelId: String? = null

    fun download() {
        if (type == Constants.TYPE_IMAGE) {
            val request = initDManager(media!!, application, channelId!!,".png")
            if (request == null) {
                defaultRepo.mSnackBarText.value = Event("Already Exists")
                return
            } else {
                val downloadManager =
                    application.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager?
                downloadManager!!.enqueue(request)
            }
        } else {
            val request = initDManager(media!!, application, channelId!!, ".mp4")
            if (request == null) {
                defaultRepo.mSnackBarText.value = Event("Already Exists")
                return
            } else {
                val downloadManager =
                    application.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager?
                downloadManager!!.enqueue(request)
                defaultRepo.mSnackBarText.value = Event("You'll be notified that download has started")
            }
        }
    }
}