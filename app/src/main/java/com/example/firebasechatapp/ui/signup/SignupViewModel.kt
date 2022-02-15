package com.example.firebasechatapp.ui.signup

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.firebasechatapp.data.repositories.AuthRepository
import com.example.firebasechatapp.data.repositories.DefaultRepository
import com.example.firebasechatapp.utils.Event
import com.example.firebasechatapp.utils.Result
import com.example.firebasechatapp.utils.isEmailValid
import com.example.firebasechatapp.utils.isTextValid
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import com.example.firebasechatapp.data.models.CreateUser
import javax.inject.Inject

@HiltViewModel
class SignupViewModel
    @Inject constructor(
        val authRepository: AuthRepository,
        val defaultRepo: DefaultRepository
        ):ViewModel() {

    private val mIsCreatedEvent = MutableLiveData<Event<Boolean>>()
    val isCreatedEvent: LiveData<Event<Boolean>> = mIsCreatedEvent

    val displayNameText = MutableLiveData<String>() // Two way
    val emailText = MutableLiveData<String>() // Two way
    val passwordText = MutableLiveData<String>() // Two way
    val isCreatingAccount = MutableLiveData<Boolean>()

    private fun createAccount() {
        isCreatingAccount.value = true
        val createUser =
            CreateUser(displayNameText.value!!, emailText.value!!, passwordText.value!!)

        authRepository.createUser(createUser) {result: Result<FirebaseUser> ->
            //the result is passed here.
            defaultRepo.onResult(null, result)
            if (result is Result.Success) {
                mIsCreatedEvent.value = Event(true)
            }
            if (result is Result.Success || result is Result.Error) isCreatingAccount.value = false
        }
    }

    fun createAccountPressed() {
        if (!isTextValid(2, displayNameText.value)) {
            defaultRepo.mSnackBarText.value = Event("Display name is too short")
            return
        }

        if (!isEmailValid(emailText.value.toString())) {
            defaultRepo.mSnackBarText.value = Event("Invalid email format")
            return
        }

        if (!isTextValid(6, passwordText.value)) {
            defaultRepo.mSnackBarText.value = Event("Password is too short")
            return
        }

        createAccount()
    }
}