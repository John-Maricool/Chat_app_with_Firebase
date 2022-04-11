package com.example.firebasechatapp.ui.chat

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.firebasechatapp.data.models.Message
import com.example.firebasechatapp.data.repositories.AuthRepository
import com.example.firebasechatapp.data.repositories.CloudRepository
import com.example.firebasechatapp.data.repositories.DefaultRepository
import com.example.firebasechatapp.data.repositories.StorageRepository
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
    val auth: AuthRepository,
    val storage: StorageRepository
) : ViewModel() {

    lateinit var channelId: String
    lateinit var media: String
    lateinit var type: String
    lateinit var otherUserId: String

    private val _done= MutableLiveData<Event<Unit>>()
    val done: LiveData<Event<Unit>> = _done

    fun sendMediaToStorage() {
        storage.putChatMedia(channelId, media) {
            defaultRepo.onResult(null, it)
            if (it is Result.Success) {
                storage.getChatMediaDownloadString(channelId, media) { downloadUri ->
                    defaultRepo.onResult(null, downloadUri)
                    if (downloadUri is Result.Success)
                        sendToDb(downloadUri.data)
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
                if(it is Result.Success)
                    _done.postValue(Event(Unit))
            }
        }
    }
}