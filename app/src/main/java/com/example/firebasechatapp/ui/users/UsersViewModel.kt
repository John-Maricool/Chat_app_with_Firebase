package com.example.firebasechatapp.ui.users

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.firebasechatapp.data.models.Message
import com.example.firebasechatapp.data.models.UserInfo
import com.example.firebasechatapp.data.repositories.AuthRepository
import com.example.firebasechatapp.data.repositories.CloudRepository
import com.example.firebasechatapp.data.repositories.DefaultRepository
import com.example.firebasechatapp.utils.Event
import com.example.firebasechatapp.utils.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UsersViewModel
@Inject constructor(
    val defaultRepo: DefaultRepository,
    val cloud: CloudRepository,
    val auth: AuthRepository
) : ViewModel() {

    private val _chats = MutableLiveData<List<UserInfo>?>()
    val chats: LiveData<List<UserInfo>?> get() = _chats

    private val _added = MutableLiveData<Boolean?>()
    val added: LiveData<Boolean?> get() = _added

    init {
        getAllUsers(auth.getUserID())
    }

    private fun getAllUsers(userId: String) {
        cloud.getAllUsers(userId) { result: Result<List<UserInfo>> ->
            defaultRepo.onResult(null, result)
            if (result is Result.Success) {
                _chats.value = result.data
            }
        }
    }

    fun isUserAdded(secUser: String) {
        viewModelScope.launch {
            cloud.isUserAddedToChats(auth.getUserID(), secUser) {
                defaultRepo.onResult(null, it)
                if (it is Result.Success) {
                    _added.value = it.data
                }
            }
        }
    }


}