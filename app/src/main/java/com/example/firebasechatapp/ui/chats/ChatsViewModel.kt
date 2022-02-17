package com.example.firebasechatapp.ui.chats

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.firebasechatapp.data.models.Message
import com.example.firebasechatapp.data.models.UserInfo
import com.example.firebasechatapp.data.repositories.CloudRepository
import com.example.firebasechatapp.data.repositories.DefaultRepository
import com.example.firebasechatapp.utils.Result
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.auth.User
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ChatsViewModel
@Inject constructor(
    var defaultRepo: DefaultRepository,
    var cloud: CloudRepository,
    var auth: FirebaseAuth
) {

    val userId = auth.currentUser!!.uid
    private val _chats = MutableLiveData<List<UserInfo>>()
    val chats: LiveData<List<UserInfo>> get() = _chats

    init {
        getChats()
    }

    private fun getChats() {
        cloud.getChats(userId) { result: Result<List<UserInfo>> ->
            if (result is Result.Success) {
                _chats.value = result.data
                result.data?.forEach { userInfo ->
                    val chatId = userInfo.id
                    cloud.getLastMessage(userId, chatId) { _message: Result<Message> ->
                        if (_message is Result.Success && _message.data != null) {
                            val message = _message.data
                            if (message.senderId != userId && (message.seen == true) || (message.seen == false)) {
                                message.seen = false
                            }
                            defaultRepo.onResult(null, _message)
                        }
                    }
                }
            }
        }
    }

    private fun getUserInfos(data: List<String>) {

    }
}