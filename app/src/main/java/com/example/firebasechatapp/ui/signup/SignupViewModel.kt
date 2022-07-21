package com.example.firebasechatapp.ui.signup

import android.content.Intent
import android.provider.MediaStore
import androidx.activity.result.ActivityResultLauncher
import androidx.lifecycle.*
import com.example.firebasechatapp.data.models.CreateUser
import com.example.firebasechatapp.data.repositories.DefaultRepository
import com.example.firebasechatapp.data.repositories.impl.SignUpRepository
import com.example.firebasechatapp.utils.Event
import com.example.firebasechatapp.utils.Result
import com.example.firebasechatapp.utils.isEmailValid
import com.example.firebasechatapp.utils.isTextValid
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SignupViewModel
@Inject constructor(
    val repo: SignUpRepository,
    val defaultRepo: DefaultRepository
) : ViewModel() {

    private val mIsCreatedEvent = MutableLiveData<Event<Boolean>>()
    val isCreatedEvent: LiveData<Event<Boolean>> = mIsCreatedEvent

    val displayNameText = MutableLiveData<String>() // Two way
    val emailText = MutableLiveData<String>() // Two way
    val passwordText = MutableLiveData<String>() // Two way
    val secondPassword = MutableLiveData<String>() //two way
    val isCreatingAccount = MutableLiveData<Boolean>()
    val imageUri = MutableLiveData<String>()

    lateinit var resultLauncher: ActivityResultLauncher<Intent>

    private fun createAccount() {
        isCreatingAccount.value = true
        val createUser =
            CreateUser(
                displayNameText.value!!.trim(),
                passwordText.value!!.trim(),
                emailText.value!!.trim()
            )

        repo.createAccount(createUser, imageUri.value!!, viewModelScope) {
            defaultRepo.onResult(null, it)
            if (it is Result.Success){
                mIsCreatedEvent.postValue(Event(true))
            }
        }
    }

    fun removeListener(owner: LifecycleOwner) {
        return repo.unListenForAuthChanges(owner)
    }

    fun createAccountPressed() {
        if (!isTextValid(5, displayNameText.value)) {
            defaultRepo.mSnackBarText.value = Event("Display name is too short")
            return
        }

        if (!isEmailValid(emailText.value.toString().trim())) {
            defaultRepo.mSnackBarText.value = Event("Error in your Email Address")
            return
        }

        if (!isTextValid(6, passwordText.value) ||
            passwordText.value != secondPassword.value ||
            !isTextValid(6, passwordText.value)
        ) {
            defaultRepo.mSnackBarText.value = Event("Error with your password")
            return
        }

        if (imageUri.value == null) {
            defaultRepo.mSnackBarText.value = Event("Please select an image")
            return
        }
        createAccount()
    }

    fun goToGallery() {
        val intent =
            Intent(Intent.ACTION_OPEN_DOCUMENT, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
        intent.type = "image/*"
        resultLauncher.launch(intent)
    }
}