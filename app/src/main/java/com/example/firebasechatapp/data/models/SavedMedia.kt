package com.example.firebasechatapp.data.models

import android.graphics.Bitmap
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class SavedMedia(
    val type: Int,
    val media: Bitmap,
    val path: String = ""
): Parcelable