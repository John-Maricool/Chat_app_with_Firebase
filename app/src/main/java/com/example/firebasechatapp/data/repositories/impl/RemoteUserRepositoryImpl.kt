package com.example.firebasechatapp.data.repositories.impl

import androidx.lifecycle.LiveData
import com.example.firebasechatapp.data.source.remote.FirebaseFirestoreSource
import com.example.firebasechatapp.data.models.UserInfo
import com.example.firebasechatapp.data.repositories.abstractions.RemoteUserRepository
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

    override suspend fun uploadUserData(userInfo: UserInfo) {
        source.uploadUserDetailsToDb(userInfo).addOnSuccessListener {
            prefs.storeUserDetails(
                name = userInfo.displayName,
                email = userInfo.email,
                photo = userInfo.profileImageUrl
            )
        }
    }

    override fun changeUserName(newName: String, b: (Result<String>) -> Unit) {
        b.invoke(Result.Loading)
        source.changeName(prefs.getUserUid()!!, newName).addOnSuccessListener {
            prefs.storeUserDetails(name = newName)
            b.invoke(Result.Success("Successful"))
        }.addOnFailureListener {
            b.invoke(Result.Error(it.toString()))
        }
    }

    override suspend fun checkIfUserHasNewMessages(userId: String): LiveData<Boolean> {
        return source.checkIfUserHasNewMessages(userId)
    }
}