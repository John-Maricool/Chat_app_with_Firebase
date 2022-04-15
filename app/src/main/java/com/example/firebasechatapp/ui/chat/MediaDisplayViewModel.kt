package com.example.firebasechatapp.ui.chat

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.firebasechatapp.data.models.Message
import com.example.firebasechatapp.data.repositories.AuthRepository
import com.example.firebasechatapp.data.repositories.CloudRepository
import com.example.firebasechatapp.data.repositories.DefaultRepository
import com.example.firebasechatapp.data.repositories.MediaDisplayRepository
import com.example.firebasechatapp.utils.Constants
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
    val cloud: CloudRepository,
    val repo: MediaDisplayRepository,
    val auth: AuthRepository
) : ViewModel() {

    lateinit var channelId: String
    lateinit var media: String
    lateinit var type: String
    lateinit var otherUserId: String

    private val _done = MutableLiveData<Event<Boolean>>()
    val done: LiveData<Event<Boolean>> = _done

    fun sendMediaToStorage() {
        viewModelScope.launch {
            repo.storeMedia(media, channelId) {
                defaultRepo.onResult(null, it)
                if (it is Result.Success) {
                    sendToDb(it.data)
                }
            }
        }
    }

    private fun sendToDb(data: String?) {
        val message =
            Message(auth.getUserID(), otherUserId, data!!, false, Date().time, Constants.TYPE_IMAGE)
        if (type == "video") {
            message.type = Constants.TYPE_VIDEO
        }

        viewModelScope.launch {
            cloud.sendMessage(channelId, message) {
                defaultRepo.onResult(null, it)
                if (it is Result.Success)
                    _done.postValue(Event(true))
            }
        }
    }
}