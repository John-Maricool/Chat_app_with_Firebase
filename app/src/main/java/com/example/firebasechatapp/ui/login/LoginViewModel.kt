package com.example.firebasechatapp.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.firebasechatapp.data.models.Login
import com.example.firebasechatapp.data.repositories.AuthRepository
import com.example.firebasechatapp.data.repositories.DefaultRepository
import com.example.firebasechatapp.utils.Event
import com.example.firebasechatapp.utils.Result
import com.example.firebasechatapp.utils.isEmailValid
import com.example.firebasechatapp.utils.isTextValid
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel
@Inject constructor(
    val authRepository: AuthRepository,
    val defaultRepo: DefaultRepository
) : ViewModel() {

    val mSnackBarText = MutableLiveData<Event<String>>()
    private val _isLoggedInEvent = MutableLiveData<Event<FirebaseUser>>()
    val isLoggedInEvent: LiveData<Event<FirebaseUser>> = _isLoggedInEvent
    val emailText = MutableLiveData<String>() // Two way
    val passwordText = MutableLiveData<String>() // Two way
    val isLoggingIn = MutableLiveData<Boolean>()

    private fun login() {
        isLoggingIn.value = true
        val login =
            Login(email = emailText.value!!, password = passwordText.value!!)

        authRepository.loginUser(login) { result: Result<FirebaseUser> ->
            defaultRepo.onResult(null, result)
            if (result is Result.Success) {
                _isLoggedInEvent.value = Event(result.data!!)
            }
            if (result is Result.Success || result is Result.Error) isLoggingIn.value = false
        }
    }

    fun loginPressed() {
        if (!isEmailValid(emailText.value.toString().trim())) {
            mSnackBarText.value = Event("Invalid email format")
            return
        }
        if (!isTextValid(6, passwordText.value)) {
            mSnackBarText.value = Event("Password is too short")
            return
        }
        login()
    }
}