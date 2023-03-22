package com.example.firebasechatapp.ui.chats

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.firebasechatapp.data.repositories.DefaultRepository
import com.example.firebasechatapp.data.source.local.UserEntity
import com.example.firebasechatapp.data.usecases.ChatsListUseCase
import com.example.firebasechatapp.utils.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatsViewModel
@Inject constructor(
    var defaultRepo: DefaultRepository,
    var repo: ChatsListUseCase,
) : ViewModel() {

    private var _chat: MutableLiveData<List<UserEntity>?> = MutableLiveData()
    var chat: LiveData<List<UserEntity>> = MutableLiveData()

    private val _channelId = MutableLiveData<String?>()
    val channelId: LiveData<String?> get() = _channelId

    fun initialize() {
        viewModelScope.launch {
            chat = repo.getAllChats()
        }
    }

    fun cacheList() {
        viewModelScope.launch {
            repo.cacheChatList {
                defaultRepo.onResult(null, it)
                if (it is Result.Success)
                    chat = it.data!!
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

    override fun onCleared() {
        super.onCleared()
        Log.d("cleared", "chats fragment")
    }
}