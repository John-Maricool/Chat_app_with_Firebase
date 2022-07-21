package com.example.firebasechatapp.ui.first

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.firebasechatapp.R
import com.example.firebasechatapp.databinding.FragmentFirstBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FirstFragment : Fragment(R.layout.fragment_first) {

    private var _binding: FragmentFirstBinding? = null
    private val binding get() = _binding!!
    private val model: FirstViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requireActivity().onBackPressedDispatcher.addCallback(object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                requireActivity().finish()
            }
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentFirstBinding.bind(view)
        binding.viewmodel = model
        binding.lifecycleOwner = this.viewLifecycleOwner

        if (model.user != null) {
            navigateToChats()
        }

        navigate()
    }

    private fun navigateToChats() {
        findNavController().navigate(R.id.chatsFragment)
    }

    private fun navigate() {
        model.loginEvent.observe(viewLifecycleOwner) {
            findNavController().navigate(R.id.action_firstFragment_to_loginFragment)
        }
        model.createAccountEvent.observe(viewLifecycleOwner) {
            findNavController().navigate(R.id.action_firstFragment_to_signUpFragment)
        }
    }

}