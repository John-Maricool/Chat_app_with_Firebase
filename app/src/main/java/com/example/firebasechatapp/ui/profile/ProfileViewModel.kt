package com.example.firebasechatapp.ui.profile

import androidx.lifecycle.ViewModel
import com.example.firebasechatapp.data.repositories.CloudRepository
import com.example.firebasechatapp.data.repositories.DefaultRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel
@Inject constructor(val defaultRepo: DefaultRepository, val cloud: CloudRepository): ViewModel(){
}