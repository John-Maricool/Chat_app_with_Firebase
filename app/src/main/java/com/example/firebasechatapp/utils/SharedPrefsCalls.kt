package com.example.firebasechatapp.utils

interface SharedPrefsCalls {
    fun getUserUid(): String?
    fun storeUserUid(id: String)
    fun resetUserUid()
    fun isNightModeOn(): Boolean
    fun storeUserDetails(name: String = "", email: String = "", photo: String = "")
    fun getUserName(): String?
    fun getUserEmail(): String?
    fun getUserPhoto(): String?
    fun changeNightMode(mode: Boolean)
}