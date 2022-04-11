package com.example.firebasechatapp.ui.firstmessage

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.firebasechatapp.data.models.Message
import com.example.firebasechatapp.data.repositories.AuthRepository
import com.example.firebasechatapp.data.repositories.CloudRepository
import com.example.firebasechatapp.data.repositories.DefaultRepository
import com.example.firebasechatapp.utils.Result
import com.example.firebasechatapp.utils.isTextValid
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class MessageViewModel
@Inject constructor(
    val repo: CloudRepository,
    val defRepo: DefaultRepository,
    val auth: AuthRepository
) : ViewModel() {

    private val _finished = MutableLiveData<String?>()
    val finished: LiveData<String?> get() = _finished
    val messageText = MutableLiveData<String>()

    fun finishUp(secUserId: String) {
        if (isTextValid(1, messageText.value)) {
            val message =
                Message(auth.getUserID(), secUserId, messageText.value!!, false, Date().time)
            send(secUserId, message)
        } else {
            return
        }
    }

    private fun send(secUserId: String, message: Message) {
        viewModelScope.launch {
            repo.createChatInfoWithFirstMessage(auth.getUserID(), secUserId, message) {
                defRepo.onResult(null, it)
                if (it is Result.Success) {
                    _finished.postValue(it.data)
                }
            }
        }
    }
}