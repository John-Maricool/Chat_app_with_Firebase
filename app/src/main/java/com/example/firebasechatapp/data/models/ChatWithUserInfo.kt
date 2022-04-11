package com.example.firebasechatapp.data.models

data class ChatWithUserInfo(
    val mChat: Chat,
    val mUserInfo: UserInfo,
){
    constructor(): this(Chat(), UserInfo())
}