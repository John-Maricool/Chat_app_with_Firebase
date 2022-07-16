package com.example.firebasechatapp.utils

import com.google.firebase.firestore.DocumentSnapshot

object Constants {
    val check_internet: String = "No Internet"
    const val chatChannels: String = "Chat Channels"
    const val lastMessage: String = "Last Message"
    const val user: String = "Users"
    //const val chats: String = "Followed Chats"
    const val messages = "Messages"
    const val chat: String = "Chats"
    const val TYPE_TEXT: Int = 0
    const val TYPE_IMAGE: Int = 1
    const val TYPE_VIDEO: Int = 2
     fun randomID(): String = List(16) {
        (('a'..'z') + ('A'..'Z') + ('0'..'9')).random()
    }.joinToString("")

    var CURRENT_SNAP: DocumentSnapshot? = null
    var currentPage = 1
}