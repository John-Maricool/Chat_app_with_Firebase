package com.example.firebasechatapp.ui.login

import android.os.Bundle
import com.example.firebasechatapp.R
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.firebasechatapp.databinding.FragmentLoginBinding
import com.example.firebasechatapp.ui.MainActivity
import com.example.firebasechatapp.utils.EventObserver
import com.example.firebasechatapp.utils.forceHideKeyboard
import com.example.firebasechatapp.utils.showSnackBar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : Fragment(R.layout.fragment_login) {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private val model: LoginViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentLoginBinding.bind(view)
        binding.lifecycleOwner = this.viewLifecycleOwner
        binding.viewmodel = model
        setUpObservers()
    }

    private fun setUpObservers() {
        model.defaultRepo.dataLoading.observe(viewLifecycleOwner,
            EventObserver { (activity as MainActivity).showGlobalProgressBar(it) })

        model.defaultRepo.snackBarText.observe(viewLifecycleOwner,
            EventObserver { text ->
                view?.showSnackBar(text)
                view?.forceHideKeyboard()
            })

        model.isLoggedInEvent.observe(viewLifecycleOwner, EventObserver {
            navigateToChats()
        })
    }

    private fun navigateToChats() {
       val action = LoginFragmentDirections.actionLoginFragmentToChatsFragment()
        findNavController().navigate(action)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}