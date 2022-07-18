package com.example.firebasechatapp.data.repositories

import com.example.firebasechatapp.data.db.remote.FirebaseFirestoreSource
import com.example.firebasechatapp.data.models.UserInfo
import com.example.firebasechatapp.utils.Result
import com.example.firebasechatapp.utils.SharedPrefsCalls

/**
 * This repo is just for user database related issues.
 * Creating and updating single user info
 */
class RemoteUserRepositoryImpl(var source: FirebaseFirestoreSource, var prefs: SharedPrefsCalls) :
    RemoteUserRepository {

    override suspend fun getUserInfo(): UserInfo? {
        return source.getUserInfo(prefs.getUserUid()!!)
            .toObject(UserInfo::class.java)
    }

    override suspend fun toggleOnline(online: Boolean) {
        prefs.getUserUid()?.let { source.toggleOnline(it, online) }
    }

    override fun changeUserName(newName: String, b: (Result<String>) -> Unit) {
        b.invoke(Result.Loading)
        source.changeName(prefs.getUserUid()!!, newName).addOnSuccessListener {
            b.invoke(Result.Success("Successful"))
        }.addOnFailureListener {
            b.invoke(Result.Error(it.toString()))
        }
    }

    override suspend fun uploadUserData(userInfo: UserInfo) {
        source.uploadUserDetailsToDb(userInfo).addOnSuccessListener {
            prefs.storeUserDetails(
                name = userInfo.displayName,
                email = userInfo.email,
                photo = userInfo.profileImageUrl
            )
        }
    }
}