package com.example.firebasechatapp.ui.signup

import android.widget.EditText
import com.google.firebase.auth.FirebaseAuth
import android.widget.ProgressBar
import android.os.Bundle
import com.example.firebasechatapp.R
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore
import android.content.Intent
import android.view.View
import android.widget.Button
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.example.firebasechatapp.MainPart
import com.example.firebasechatapp.databinding.FragmentSignUpBinding
import com.google.firebase.firestore.DocumentReference
import dagger.hilt.android.AndroidEntryPoint
import java.util.HashMap

@AndroidEntryPoint
class SignUpFragment : Fragment(R.layout.fragment_sign_up) {

    private var _binding: FragmentSignUpBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentSignUpBinding.bind(view)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}