package com.example.firebasechatapp.data.usecases

import com.example.firebasechatapp.data.source.local.UserDao
import com.example.firebasechatapp.data.repositories.abstractions.AuthRepository
import com.example.firebasechatapp.data.repositories.abstractions.RemoteUserRepository
import com.example.firebasechatapp.utils.SharedPrefsCalls
import javax.inject.Inject

class SettingsUseCase
@Inject constructor(
    private val auth: AuthRepository,
    private val prefs: SharedPrefsCalls,
    private val cloud: RemoteUserRepository,
    private val dao: UserDao
) {
    fun userName() = prefs.getUserName()
    fun userEmail() = prefs.getUserEmail()
    fun userImg() = prefs.getUserPhoto()
    val nightModeState = prefs.isNightModeOn()

    suspend fun performLogout() {
        cloud.toggleOnline(false)
        auth.signOut()
        prefs.resetUserUid()
        dao.deleteAll()
    }

    fun changeNightMode(state: Boolean) {
        prefs.changeNightMode(state)
    }
}