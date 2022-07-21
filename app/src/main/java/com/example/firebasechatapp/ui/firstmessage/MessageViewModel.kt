package com.example.firebasechatapp.ui.firstmessage

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.firebasechatapp.data.repositories.DefaultRepository
import com.example.firebasechatapp.data.repositories.impl.FirstMessageRepository
import com.example.firebasechatapp.utils.Result
import com.example.firebasechatapp.utils.isTextValid
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MessageViewModel
@Inject constructor(
    val repo: FirstMessageRepository,
    val defRepo: DefaultRepository,
) : ViewModel() {

    private val _finished = MutableLiveData<String?>()
    val finished: LiveData<String?> get() = _finished
    val messageText = MutableLiveData<String>()

    fun finishUp(secUserId: String) {
        if (isTextValid(1, messageText.value)) {
            send(secUserId, messageText.value!!)
        } else {
            return
        }
    }

    private fun send(secUserId: String, message: String) {
        viewModelScope.launch {
            repo.send(secUserId, message) {
                defRepo.onResult(null, it)
                if (it is Result.Success) {
                    _finished.postValue(it.data)
                }
            }
        }
    }
}