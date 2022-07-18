package com.example.firebasechatapp.ui.signup

import android.app.Activity
import android.os.Bundle
import com.example.firebasechatapp.R
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.firebasechatapp.databinding.FragmentSignUpBinding
import com.example.firebasechatapp.ui.app_components.MainActivity
import com.example.firebasechatapp.utils.EventObserver
import com.example.firebasechatapp.utils.forceHideKeyboard
import com.example.firebasechatapp.utils.showSnackBar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignUpFragment : Fragment(R.layout.fragment_sign_up) {

    private var _binding: FragmentSignUpBinding? = null
    private val binding get() = _binding!!
    private val model: SignupViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        model.resultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    model.imageUri.value = result.data?.data.toString()
                }
            }
        requireActivity().onBackPressedDispatcher.addCallback(object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().navigate(R.id.firstFragment)
            }
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentSignUpBinding.bind(view)
        binding.viewmodel = model
        binding.lifecycleOwner = this.viewLifecycleOwner
        setupObservers()
    }

    private fun setupObservers() {
        model.defaultRepo.dataLoading.observe(viewLifecycleOwner,
            EventObserver {
                (activity as MainActivity).showGlobalProgressBar(it)
            })

        model.defaultRepo.snackBarText.observe(viewLifecycleOwner,
            EventObserver { text ->
                view?.showSnackBar(text)
                view?.forceHideKeyboard()
            })

        model.isCreatedEvent.observe(viewLifecycleOwner, EventObserver {
            when(it){
                true -> {navigateToChats()}
            }
        })
    }

    private fun navigateToChats() {
        findNavController().navigate(R.id.chatsFragment)
    }

    override fun onDestroy() {
        super.onDestroy()
        model.removeListener(this)
        _binding = null
    }
}