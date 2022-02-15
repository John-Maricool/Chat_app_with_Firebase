package com.example.firebasechatapp.ui.chats

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.firebasechatapp.R
import com.example.firebasechatapp.databinding.FragmentChatsBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChatsFragment : Fragment(R.layout.fragment_chats) {
    private var _binding: FragmentChatsBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentChatsBinding.bind(view)
       // binding.viewmodel = model
        //binding.lifecycleOwner = this.viewLifecycleOwner

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}

