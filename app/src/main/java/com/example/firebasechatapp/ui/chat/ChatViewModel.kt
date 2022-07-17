package com.example.firebasechatapp.ui.chat

import androidx.lifecycle.*
import com.example.firebasechatapp.data.models.Message
import com.example.firebasechatapp.data.models.UserInfo
import com.example.firebasechatapp.data.repositories.AuthRepository
import com.example.firebasechatapp.data.repositories.CloudRepository
import com.example.firebasechatapp.data.repositories.DefaultRepository
import com.example.firebasechatapp.utils.Constants
import com.example.firebasechatapp.utils.Event
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
    var CURRENT_SCROLL_POSITION: MutableLiveData<Int> = MutableLiveData(0)
    var channelId: String? = null

    private val _messages = MediatorLiveData<List<Message>?>()
    val messages: LiveData<List<Message>?> get() = _messages

    private val _olderMessages = MediatorLiveData<List<Message>?>()
    val olderMessages: LiveData<List<Message>?> get() = _olderMessages

    private val _userInfo = MutableLiveData<UserInfo?>()
    val userInfo: LiveData<UserInfo?> get() = _userInfo

    private var isOpened = MutableLiveData(false)
    val opened: LiveData<Boolean> get() = isOpened

    private val _media = MutableLiveData<Event<Boolean>>()
    val media: LiveData<Event<Boolean>> get() = _media

    fun getAllMessages() {
        cloud.getAllMessages(auth.getUserID(), channelId!!) { res ->
            defaultRepo.onResult(null, res)
            if (res is Result.Success) {
                _messages.postValue(res.data)
                CURRENT_SCROLL_POSITION.value = res.data?.size!! - 1
            }
        }
    }

    fun loadNewPage() {
        viewModelScope.launch {
            cloud.reloadNewPageOfMessages(channelId!!) { res ->
                if (res is Result.Success) {
                    _olderMessages.postValue(res.data)
                    CURRENT_SCROLL_POSITION.value = res.data!!.size.minus(messages.value!!.size)
                }
            }
        }
    }

    fun toggleOpenOptions() {
        isOpened.value = !isOpened.value!!
    }

    fun sendMessage(): Message? {
        return if (isTextValid(1, messageText.value)) {
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
            message
        } else {
            null
        }
    }

    fun goToMedia() {
        _media.value = Event(true)
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