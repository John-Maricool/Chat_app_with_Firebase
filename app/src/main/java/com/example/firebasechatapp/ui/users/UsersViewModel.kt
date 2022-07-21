package com.example.firebasechatapp.ui.users

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.firebasechatapp.data.models.UserInfo
import com.example.firebasechatapp.data.repositories.abstractions.AuthRepository
import com.example.firebasechatapp.data.repositories.DefaultRepository
import com.example.firebasechatapp.data.repositories.abstractions.UsersAndChatsRepository
import com.example.firebasechatapp.data.repositories.impl.UsersListRepository
import com.example.firebasechatapp.utils.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class UsersViewModel
@Inject constructor(
    val defaultRepo: DefaultRepository,
    @Named("users") val repo: UsersAndChatsRepository,
    val auth: AuthRepository
) : ViewModel() {

    private var _chats = MutableLiveData<List<UserInfo>?>()
    val chats: LiveData<List<UserInfo>?> get() = _chats

    private val _added = MutableLiveData<Boolean?>()
    val added: LiveData<Boolean?> get() = _added

    init {
        getAllUsers()
    }

    private fun getAllUsers() {
        repo.getList { result: Result<List<UserInfo>> ->
            defaultRepo.onResult(null, result)
            if (result is Result.Success) {
                _chats.value = result.data
            }
        }
    }

    fun isUserAdded(secUser: String) {
        viewModelScope.launch {
            (repo as UsersListRepository).isUserAdded(secUser) {
                defaultRepo.onResult(null, it)
                if (it is Result.Success) {
                    _added.value = it.data
                }
            }
        }
    }
}