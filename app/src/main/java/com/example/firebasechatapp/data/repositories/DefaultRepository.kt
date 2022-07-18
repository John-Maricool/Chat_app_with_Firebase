package com.example.firebasechatapp.data.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.firebasechatapp.utils.Event
import com.example.firebasechatapp.utils.Result
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DefaultRepository @Inject constructor() {

    val mSnackBarText = MutableLiveData<Event<String>>()
    val snackBarText: LiveData<Event<String>> = mSnackBarText

    private val mDataLoading = MutableLiveData<Event<Boolean>>()
    val dataLoading: LiveData<Event<Boolean>> = mDataLoading

    fun <T> onResult(mutableLiveData: MutableLiveData<T>? = null, result: Result<T>) {
        when (result) {
            is Result.Loading -> mDataLoading.postValue(Event(true))

            is Result.Error -> {
                mDataLoading.postValue(Event(false))
                result.msg?.let { mSnackBarText.postValue(Event(it)) }
            }

            is Result.Success -> {
                mDataLoading.value = Event(false)
                // result.data?.let { mutableLiveData?.value = it }
                result.msg?.let { mSnackBarText.postValue(Event(it)) }
            }
        }
    }
}