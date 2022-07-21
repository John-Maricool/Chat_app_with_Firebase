package com.example.firebasechatapp.ui.chat

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.firebasechatapp.data.models.Message
import com.example.firebasechatapp.data.repositories.DefaultRepository
import com.example.firebasechatapp.data.usecases.MediaDisplayUseCase
import com.example.firebasechatapp.utils.Constants.TYPE_IMAGE
import com.example.firebasechatapp.utils.Event
import com.example.firebasechatapp.utils.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class MediaDisplayViewModel
@Inject constructor(
    val defaultRepo: DefaultRepository,
    val repo: MediaDisplayUseCase
) : ViewModel() {

    lateinit var channelId: String
    lateinit var media: Bitmap
    lateinit var otherUserId: String

    private val _done = MutableLiveData<Event<Boolean>>()
    val done: LiveData<Event<Boolean>> = _done

    fun sendMediaToStorage() {
        viewModelScope.launch {
            repo.storeMedia(media, channelId) {
                defaultRepo.onResult(null, it)
                if (it is Result.Success) {
                    sendToDb(it.data!!)
                }
            }
        }
    }

    private fun sendToDb(data: String) {
        val message =
            Message(repo.uid!!, otherUserId, data, false, Date().time, TYPE_IMAGE)
        viewModelScope.launch {
            repo.sendMessage(channelId, message) {
                defaultRepo.onResult(null, it)
                if (it is Result.Success)
                    _done.postValue(Event(true))
            }
        }
    }
}