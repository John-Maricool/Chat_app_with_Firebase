package com.example.firebasechatapp.data.usecases

import com.example.firebasechatapp.data.source.remote.FirebaseFirestoreSource
import com.example.firebasechatapp.data.models.UserInfo
import com.example.firebasechatapp.utils.Result
import com.example.firebasechatapp.utils.SharedPrefsCalls
import javax.inject.Inject

class UsersListUseCase
@Inject constructor(var prefs: SharedPrefsCalls, var source: FirebaseFirestoreSource) {
    fun getList(b: (Result<List<UserInfo>>) -> Unit) {
        b.invoke(Result.Loading)
        source.getAllUsers(prefs.getUserUid()!!).addSnapshotListener { value, error ->
            if (value != null) {
                val user = value.toObjects(UserInfo::class.java)
                b.invoke(Result.Success(user))
            } else {
                b.invoke(Result.Error(error.toString()))
            }
        }
    }

    suspend fun isUserAdded(secUser: String, b: (Result<Boolean>) -> Unit) {
        b.invoke(Result.Loading)
        try {
            val result = source.isUserAddedToChats(prefs.getUserUid()!!, secUser)
            if (result != null && result.exists()) {
                b.invoke(Result.Success(true))
            } else {
                b.invoke(Result.Success(false))
            }
        } catch (e: Exception) {
            b.invoke(Result.Error(e.toString()))
        }
    }
}




