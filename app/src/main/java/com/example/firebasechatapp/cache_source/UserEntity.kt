package com.example.firebasechatapp.cache_source

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.firebasechatapp.utils.Constants

@Entity
data class UserEntity(
    @PrimaryKey(autoGenerate = false) var channelId: String = "",
    var id: String,
    var displayName: String,
    var profileImageUrl: String,
    var online: Boolean,
    var lastMessage:String = "",
    var sentTime: Long,
    var receiverId: String,
    var seen: Boolean?,
    var type: Int = Constants.TYPE_TEXT
)