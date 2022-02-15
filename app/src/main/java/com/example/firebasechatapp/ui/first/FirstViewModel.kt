package com.example.firebasechatapp.ui.first

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.firebasechatapp.utils.Event
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FirstViewModel
    @Inject constructor(auth: FirebaseAuth): ViewModel() {

    val user = auth.currentUser

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
}