package com.example.firebasechatapp

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.firebasechatapp.databinding.FragmentLoginBinding
import com.example.firebasechatapp.databinding.FragmentMessageBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MessageFragment : Fragment(R.layout.fragment_message) {
    private var _binding: FragmentMessageBinding? = null
    private val binding get() = _binding!!


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentMessageBinding.bind(view)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}