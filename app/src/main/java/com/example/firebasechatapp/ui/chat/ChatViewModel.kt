package com.example.firebasechatapp.ui.chat

import androidx.lifecycle.*
import com.example.firebasechatapp.data.models.Message
import com.example.firebasechatapp.data.models.UserInfo
import com.example.firebasechatapp.data.repositories.AuthRepository
import com.example.firebasechatapp.data.repositories.CloudRepository
import com.example.firebasechatapp.data.repositories.DefaultRepository
import com.example.firebasechatapp.utils.Constants
import com.example.firebasechatapp.utils.Result
import com.example.firebasechatapp.utils.isTextValid
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class ChatViewModel
@Inject constructor(
    val defaultRepo: DefaultRepository,
    val cloud: CloudRepository,
    val auth: AuthRepository
) : ViewModel() {

    val messageText = MutableLiveData<String?>()

    var otherUserId: String? = null
    var channelId: String? = null
    private val _messages = MediatorLiveData<List<Message>?>()
    val messages: LiveData<List<Message>?> get() = _messages

    private val _userInfo = MutableLiveData<UserInfo?>()
    val userInfo: LiveData<UserInfo?> get() = _userInfo

    private var isOpened = MutableLiveData(false)
    val opened: LiveData<Boolean> get() = isOpened

    fun getAllMessages() {
        cloud.getAllMessages(auth.getUserID(), channelId!!) { res ->
            defaultRepo.onResult(null, res)
            if (res is Result.Success)
                _messages.postValue(res.data)
        }
    }

    fun toggleOpenOptions() {
        isOpened.value = !isOpened.value!!
    }

    fun sendMessage() {
        if (isTextValid(1, messageText.value)) {
            val message = Message(
                auth.getUserID(), otherUserId!!,
                messageText.value!!, false, Date().time, Constants.TYPE_TEXT
            )
            viewModelScope.launch {
                cloud.sendMessage(channelId!!, message) {
                    defaultRepo.onResult(null, it)
                }
            }
            messageText.value = null
        } else {
            return
        }
    }

    fun getUserInfo(otherUserId: String) {
        cloud.getChangedUserInfo(otherUserId) {
            defaultRepo.onResult(null, it)
            if (it is Result.Success) {
                _userInfo.value = it.data
            }
        }
    }
}