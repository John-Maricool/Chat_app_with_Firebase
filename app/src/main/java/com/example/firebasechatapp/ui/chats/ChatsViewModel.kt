package com.example.firebasechatapp.ui.chats

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.firebasechatapp.cache_source.UserEntity
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

    var chat: LiveData<List<UserEntity>?> = MutableLiveData<List<UserEntity>?>()

    private val _channelId = MutableLiveData<String?>()
    val channelId: LiveData<String?> get() = _channelId

    fun initialize(){
        chat = repo.getAllChats()
        viewModelScope.launch {
            repo.cacheChatList()
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