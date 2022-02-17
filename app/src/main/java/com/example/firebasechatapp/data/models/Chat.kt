package com.example.firebasechatapp.data.models

import java.util.*

data class Chat(
    val lastMessage: Message,
    val info: ChatInfo
)

data class ChatInfo(
    var id: String = ""
)

data class Message(
    var senderId: String = "",
    var text: String = "",
    var seen: Boolean? = null,
    var sentTime: Long = Date().time
)