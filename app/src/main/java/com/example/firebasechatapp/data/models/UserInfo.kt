package com.example.firebasechatapp.data.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class UserInfo(
    var id: String,
    var displayName: String,
    var email: String,
    var lastSeen: Long,
    var profileImageUrl: String,
    var online: Boolean
): Parcelable{
    constructor(): this("", "", "", 0, "", false)
}


data class UserDetails(
    var displayName: String,
    var email: String,
    var profileImageUrl: String){
    constructor(): this("", "", "")
}