package com.example.firebasechatapp.ui.changeDetails

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.firebasechatapp.data.repositories.abstractions.CloudRepository
import com.example.firebasechatapp.data.repositories.DefaultRepository
import com.example.firebasechatapp.data.repositories.abstractions.RemoteUserRepository
import com.example.firebasechatapp.utils.Event
import com.example.firebasechatapp.utils.Result
import com.example.firebasechatapp.utils.isTextValid
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ChangeNameViewModel
@Inject constructor(
    val defaultRepo: DefaultRepository,
    val cloud: RemoteUserRepository
) : ViewModel() {

    val text = MutableLiveData<String>()
    val email = MutableLiveData<String>()

    private val _done =  MutableLiveData<String>()
    val done: LiveData<String> get() = _done

    fun changeName(){
        if (isTextValid(2, text.value)){
            changeUserName(text.value!!)
        }else{
            defaultRepo.mSnackBarText.value = Event("Invalid Name")
        }
    }

    private fun changeUserName(newName: String) {
        cloud.changeUserName(newName){
            defaultRepo.onResult(null, it)
            if (it is Result.Success){
                _done.value = "Successful"
            }
        }
    }
}