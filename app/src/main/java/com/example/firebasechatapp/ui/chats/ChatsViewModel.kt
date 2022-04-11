package com.example.firebasechatapp.ui.chats

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.firebasechatapp.data.models.ChatWithUserInfo
import com.example.firebasechatapp.data.repositories.CloudRepository
import com.example.firebasechatapp.data.repositories.DefaultRepository
import com.example.firebasechatapp.utils.Result
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ChatsViewModel
@Inject constructor(
    var defaultRepo: DefaultRepository,
    var cloud: CloudRepository,
    var auth: FirebaseAuth
) : ViewModel() {

    val userId = auth.currentUser!!.uid
    private val _chats = MutableLiveData<List<ChatWithUserInfo>?>()
    val chat: LiveData<List<ChatWithUserInfo>?> get() = _chats

    private val _channelId = MutableLiveData<String?>()
    val channelId: LiveData<String?> get() = _channelId

    init {
        getAllChats()
    }

    private fun getAllChats() {
        cloud.getChatsWithUserInfo(userId) { result ->
            defaultRepo.onResult(null, result)
            if (result is Result.Success) {
                _chats.value = result.data
            }
        }
    }

    fun getChannelId(otherId: String){
        cloud.getChatChannel(userId, otherId){
            defaultRepo.onResult(null, it)
            if (it is Result.Success)
                _channelId.value = it.data
        }
    }
}