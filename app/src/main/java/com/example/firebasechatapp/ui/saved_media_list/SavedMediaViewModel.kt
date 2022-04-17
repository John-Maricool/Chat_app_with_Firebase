package com.example.firebasechatapp.ui.saved_media_list

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.firebasechatapp.data.models.SavedMedia
import com.example.firebasechatapp.data.repositories.DefaultRepository
import com.example.firebasechatapp.ui.mediaDisplay.getAll
import com.example.firebasechatapp.utils.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

@HiltViewModel
class SavedMediaViewModel
@Inject constructor(
    @ApplicationContext val context: Context,
    val defaultRepo: DefaultRepository
) : ViewModel() {
    var channelId: String? = null
    private val _result = MutableLiveData<List<SavedMedia>?>()
    val result: LiveData<List<SavedMedia>?> get() = _result

    fun getStoredMedia() {
        getAll(context, channelId!!) {
            defaultRepo.onResult(null, it)
            if (it is Result.Success)
                _result.value = it.data
        }
    }

    override fun onCleared() {
        super.onCleared()
        _result.value = null
    }
}