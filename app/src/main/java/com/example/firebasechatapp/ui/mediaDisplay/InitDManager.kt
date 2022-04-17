package com.example.firebasechatapp.ui.mediaDisplay

import android.app.DownloadManager
import android.content.Context
import android.graphics.BitmapFactory
import android.media.ThumbnailUtils
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import com.example.firebasechatapp.data.models.SavedMedia
import com.example.firebasechatapp.utils.Constants
import com.example.firebasechatapp.utils.Result
import java.io.File


fun initDManager(url: String, context: Context, id: String, ext: String): DownloadManager.Request? {

   // val fileName = Constants.randomID()
    var fileName = url.replace("/", "")
    fileName = fileName.replace("-", "")
    Log.d("substring", fileName)
    val file = File(context.getExternalFilesDir(""), "media/${id}")
    val root = File(file.absolutePath, "${fileName}${ext}")
    if (!file.exists())
        root.mkdir()
    val request = DownloadManager.Request(Uri.parse(url))
        .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
        .setDestinationUri(Uri.fromFile(root))
        .setTitle("Media")
        .setDescription("Downloading")
        .setAllowedOverMetered(true)
        .setAllowedOverRoaming(true)
    return if (root.exists()) {
        null
    } else {
        request
    }
}

fun getAll(context: Context, id: String, b: (Result<List<SavedMedia>>) -> Unit){
    b.invoke(Result.Loading)
    try {
        val file = File(context.getExternalFilesDir(""), "media/${id}")
        val all = file.absolutePath
        val allImages = File(all)
        val allFiles = allImages.listFiles()

        val medias = mutableListOf<SavedMedia>()
        if (allFiles == null || allFiles.isEmpty() || !file.exists()) {
            b.invoke(Result.Error("No data"))
        } else {
            for (i in allFiles) {
                if (i.exists() && i != null) {
                    if (i.absolutePath.endsWith(".png")) {
                        val it = BitmapFactory.decodeFile(i.absolutePath)
                        val media = SavedMedia(Constants.TYPE_IMAGE, it)
                        medias.add(media)
                    } else {
                        val thumb = ThumbnailUtils.createVideoThumbnail(
                            i.absolutePath,
                            MediaStore.Video.Thumbnails.MINI_KIND
                        )
                        val media = SavedMedia(Constants.TYPE_VIDEO, thumb!!, i.absolutePath)
                        medias.add(media)
                    }
                }
            }
            b.invoke(Result.Success(medias))
        }
    }catch (e: Exception){
        b.invoke(Result.Error(e.toString()))
    }
}

