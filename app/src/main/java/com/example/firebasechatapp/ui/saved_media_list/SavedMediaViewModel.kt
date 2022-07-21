package com.example.firebasechatapp.ui.saved_media_list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.firebasechatapp.data.repositories.DefaultRepository
import com.example.firebasechatapp.data.repositories.abstractions.StorageRepository
import com.example.firebasechatapp.data.repositories.impl.StorageRepositoryImpl
import com.example.firebasechatapp.utils.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SavedMediaViewModel
@Inject constructor(
    val storage: StorageRepository,
    val defaultRepo: DefaultRepository
) : ViewModel() {
    private val _result = MutableLiveData<List<String>?>()
    val result: LiveData<List<String>?> get() = _result

    fun getStoredMedia(channelId: String) {
        viewModelScope.launch {
            (storage as StorageRepositoryImpl).getAllImages(channelId) {
                defaultRepo.onResult(null, it)
                if (it is Result.Success)
                    _result.value = it.data
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        _result.value = null
    }
}