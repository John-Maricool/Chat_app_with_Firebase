package com.example.firebasechatapp.ui.first

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.firebasechatapp.R
import com.example.firebasechatapp.databinding.FragmentFirstBinding
import com.example.firebasechatapp.databinding.FragmentLoginBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FirstFragment : Fragment(R.layout.fragment_first) {

    private var _binding: FragmentFirstBinding? = null
    private val binding get() = _binding!!
    private val model: FirstViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentFirstBinding.bind(view)
        binding.viewmodel = model
        binding.lifecycleOwner = this.viewLifecycleOwner

        if(model.user != null){
            navigateToChats()
        }
        navigate()
    }

    private fun navigateToChats() {
       findNavController().navigate(R.id.chatsFragment)
    }

    private fun navigate() {
        model.loginEvent.observe(viewLifecycleOwner) {
            findNavController().navigate(R.id.loginFragment)
        }
        model.createAccountEvent.observe(viewLifecycleOwner) {
            findNavController().navigate(R.id.signUpFragment)
        }

    }

}