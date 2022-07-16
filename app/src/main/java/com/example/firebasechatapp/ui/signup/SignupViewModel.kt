package com.example.firebasechatapp.ui.signup

import android.content.Intent
import android.provider.MediaStore
import androidx.activity.result.ActivityResultLauncher
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.firebasechatapp.data.models.CreateUser
import com.example.firebasechatapp.data.models.UserInfo
import com.example.firebasechatapp.data.repositories.AuthRepository
import com.example.firebasechatapp.data.repositories.CloudRepository
import com.example.firebasechatapp.data.repositories.DefaultRepository
import com.example.firebasechatapp.data.repositories.StorageRepository
import com.example.firebasechatapp.utils.Event
import com.example.firebasechatapp.utils.Result
import com.example.firebasechatapp.utils.isEmailValid
import com.example.firebasechatapp.utils.isTextValid
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.*
import javax.inject.Inject

@HiltViewModel
class SignupViewModel
@Inject constructor(
    val authRepository: AuthRepository,
    val defaultRepo: DefaultRepository,
    private val cloud: CloudRepository,
    private val storage: StorageRepository
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
        authRepository.createUser(createUser) { result: Result<FirebaseUser> ->
            //the result is passed here.
            defaultRepo.onResult(null, result)
            if (result is Result.Success) {
                getAuthStateListener()
            }
            if (result is Result.Success || result is Result.Error) isCreatingAccount.value = false
        }
    }

    private fun getAuthStateListener() {
        authRepository.getUser().observeForever {
            if (it != null) {
                val uid = it.uid
                storage.putUserImage(uid, imageUri.value!!) { result ->
                    defaultRepo.onResult(null, result)
                    if (result is Result.Success) {
                        storage.getUserImageDownloadString(uid, imageUri.value!!) { upload ->
                            defaultRepo.onResult(null, result)
                            if (upload is Result.Success) {
                                val user = UserInfo(
                                    uid, displayNameText.value!!, emailText.value!!, Date().time,
                                    upload.data!!, true
                                )
                                cloud.saveUserDetailsToDb(user) { res ->
                                    defaultRepo.onResult(null, res)
                                    if (res is Result.Success) {
                                        mIsCreatedEvent.value = Event(true)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
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