package com.example.firebasechatapp.data.models

import com.example.firebasechatapp.utils.Constants
import java.util.*

data class Chat(
    val lastMessage: Message,
    val channelId: String
) {
    constructor() : this(Message(), "")
}

data class ChatInfo(
    var id: String,
    var mainUserId: String,
    var secUserId: String,
) {
    constructor() : this("", "", "")
}

data class UId(
    val id: String
) {
    constructor() : this("")
}

data class Message(
    var senderId: String,
    var receiverId: String,
    var text: String,
    var seen: Boolean?,
    var sentTime: Long,
    var type: Int = Constants.TYPE_TEXT
) {
    constructor() : this("", "", "", null, 0, Constants.TYPE_TEXT)
}


