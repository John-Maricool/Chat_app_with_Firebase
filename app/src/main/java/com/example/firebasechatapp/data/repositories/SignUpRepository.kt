package com.example.firebasechatapp.data.repositories

import androidx.lifecycle.LifecycleOwner
import com.example.firebasechatapp.data.models.CreateUser
import com.example.firebasechatapp.data.models.UserInfo
import com.example.firebasechatapp.utils.Result
import com.example.firebasechatapp.utils.SharedPrefsCalls
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

class SignUpRepository
@Inject constructor(
    private val auth: AuthRepository,
    private val remotedb: RemoteUserRepository,
    private val storage: StorageRepository,
    private val prefs: SharedPrefsCalls
) {

    fun createAccount(
        user: CreateUser,
        userImg: String,
        scope: CoroutineScope,
        b: ((Result<String>) -> Unit)
    ) {
        b.invoke(Result.Loading)
        try {
            auth.createUser(createUser = user)
            listenForUserChange(user, userImg, scope) {
                b.invoke(it)
               /* if (it is Result.Success) {
                    b.invoke(Result.Success("Successful"))
                }*/
            }
        } catch (e: Exception) {
            b.invoke(Result.Error(e.toString()))
        }
    }

    private fun listenForUserChange(
        user: CreateUser,
        uri: String, scope: CoroutineScope,
        b: ((Result<String>) -> Unit)
    ) {
        b.invoke(Result.Loading)
        auth.getUser().observeForever {
            if (it != null) {
                prefs.storeUserUid(it.uid)
                try {
                    scope.launch {
                        val res = scope.launch {
                            storage.putUserImage(it.uid, uri)
                            val downloadUri = storage.getUserImageDownloadString(it.uid)
                            val userInfo = UserInfo(
                                it.uid, user.displayName, user.email, Date().time,
                                downloadUri.toString(), true
                            )
                            remotedb.uploadUserData(userInfo)
                        }
                        res.join()
                        if (res.isCompleted)
                            b.invoke(Result.Success("Success"))
                    }
                } catch (e: Exception) {
                    b.invoke(Result.Error(e.toString()))
                }
            }
        }
    }

    fun unListenForAuthChanges(owner: LifecycleOwner) {
        auth.getUser().removeObservers(owner)
    }
}