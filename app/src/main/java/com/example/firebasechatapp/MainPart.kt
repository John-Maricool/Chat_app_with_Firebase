package com.example.firebasechatapp

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.firebasechatapp.databinding.FragmentLoginBinding
import com.example.firebasechatapp.databinding.MainPartLayoutBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainPart : Fragment(R.layout.main_part_layout) {

    private var _binding: MainPartLayoutBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = MainPartLayoutBinding.bind(view)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}