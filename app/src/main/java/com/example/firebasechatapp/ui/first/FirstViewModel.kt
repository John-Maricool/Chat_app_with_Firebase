package com.example.firebasechatapp.ui.first

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.firebasechatapp.utils.Event
import com.example.firebasechatapp.utils.SharedPrefsCalls
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FirstViewModel
@Inject constructor(prefs: SharedPrefsCalls) : ViewModel() {

    val user = prefs.getUserUid()

    private val _loginEvent = MutableLiveData<Event<Unit>>()
    private val _createAccountEvent = MutableLiveData<Event<Unit>>()

    val loginEvent: LiveData<Event<Unit>> = _loginEvent
    val createAccountEvent: LiveData<Event<Unit>> = _createAccountEvent

    fun goToLoginPressed() {
        _loginEvent.value = Event(Unit)
    }

    fun goToCreateAccountPressed() {
        _createAccountEvent.value = Event(Unit)
    }

    override fun onCleared() {
        super.onCleared()
        Log.d("cleared", "first fragment")
    }
}