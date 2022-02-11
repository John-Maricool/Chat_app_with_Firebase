package com.example.firebasechatapp.ui.users

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.firebasechatapp.R
import com.example.firebasechatapp.databinding.FragmentLoginBinding
import com.example.firebasechatapp.databinding.FragmentUsersBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UsersFragment : Fragment(R.layout.fragment_users) {
    private var _binding: FragmentUsersBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentUsersBinding.bind(view)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}

