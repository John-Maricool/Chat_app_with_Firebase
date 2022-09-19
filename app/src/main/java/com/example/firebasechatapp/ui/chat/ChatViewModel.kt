package com.example.firebasechatapp.ui.chat

import androidx.lifecycle.*
import com.example.firebasechatapp.data.models.Message
import com.example.firebasechatapp.data.models.UserInfo
import com.example.firebasechatapp.data.repositories.DefaultRepository
import com.example.firebasechatapp.data.usecases.ChatUseCase
import com.example.firebasechatapp.utils.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class ChatViewModel
@Inject constructor(
    val defaultRepo: DefaultRepository,
    val cloud: ChatUseCase,
    val prefs: SharedPrefsCalls
) : ViewModel() {

    val messageText = MutableLiveData<String?>()
    var otherUserId: String? = null
    //var currentScrollPos: Int? = null

    private val _messages = MediatorLiveData<List<Message>?>()
    val messages: LiveData<List<Message>?> get() = _messages

    //private val _olderMessages = MediatorLiveData<List<Message>?>()
   // val olderMessages: LiveData<List<Message>?> get() = _olderMessages

    private val _userInfo = MutableLiveData<UserInfo?>()
    val userInfo: LiveData<UserInfo?> get() = _userInfo

    private val _media = MutableLiveData<Event<Boolean>>()
    val media: LiveData<Event<Boolean>> get() = _media

    fun getAllMessages(channelId: String) {
        cloud.getAllMessages(channelId) { res ->
            defaultRepo.onResult(null, res)
            if (res is Result.Success) {
                _messages.postValue(res.data)
              //  currentScrollPos = res.data?.size!! - 1
            }
        }
    }

  /*  fun loadNewPage(channelId: String) {
        viewModelScope.launch {
            cloud.loadOldMessages(channelId) { res ->
                if (res is Result.Success) {
                    _olderMessages.postValue(res.data)
                    currentScrollPos = res.data!!.size.minus(messages.value!!.size)
                }
            }
        }
    }*/

    fun sendMessage(channelId: String) {
        if (isTextValid(1, messageText.value)) {
            val message = Message(
                prefs.getUserUid()!!, otherUserId!!,
                messageText.value!!, false, Date().time, Constants.TYPE_TEXT
            )
            viewModelScope.launch {
                cloud.sendMessage(channelId, message) {
                    defaultRepo.onResult(null, it)
                }
            }
            messageText.value = null
        }
    }

    fun goToMedia() {
        _media.value = Event(true)
    }

    fun getUserInfo(otherUserId: String) {
        cloud.getUserInfo(otherUserId) {
            defaultRepo.onResult(null, it)
            if (it is Result.Success) {
                _userInfo.value = it.data
            }
        }
    }

   /* override fun onCleared() {
        super.onCleared()
        currentScrollPos = null
    }*/
}