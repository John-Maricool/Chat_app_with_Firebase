package com.example.firebasechatapp.ui.chats

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.firebasechatapp.cache_source.UserEntity
import com.example.firebasechatapp.data.models.ChatWithUserInfo
import com.example.firebasechatapp.data.repositories.ChatsListRepository
import com.example.firebasechatapp.data.repositories.DefaultRepository
import com.example.firebasechatapp.utils.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatsViewModel
@Inject constructor(
    var defaultRepo: DefaultRepository,
    var repo: ChatsListRepository
) : ViewModel() {

    private val _chats = MutableLiveData<List<UserEntity>?>()
    val chat: LiveData<List<UserEntity>?> get() = _chats

    private val _channelId = MutableLiveData<String?>()
    val channelId: LiveData<String?> get() = _channelId

    init {
        viewModelScope.launch {
           _chats.postValue(repo.getAllChats())
            repo.getAllChats {
                defaultRepo.onResult(null, it)
                if (it is Result.Success)
                    _chats.postValue(it.data)
            }
        }
    }

    fun getChannelId(otherId: String) {
        viewModelScope.launch {
            repo.getChannelId(otherId) {
                defaultRepo.onResult(null, it)
                if (it is Result.Success)
                    _channelId.value = it.data
            }
        }
    }
}