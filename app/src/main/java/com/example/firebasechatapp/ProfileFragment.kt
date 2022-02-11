package com.example.firebasechatapp

import android.widget.TextView
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.StorageTask
import android.view.LayoutInflater
import android.view.ViewGroup
import android.os.Bundle
import com.example.firebasechatapp.R
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.firestore.DocumentSnapshot
import com.bumptech.glide.Glide
import android.content.Intent
import com.example.firebasechatapp.ProfileFragment
import android.content.ContentResolver
import android.webkit.MimeTypeMap
import android.app.ProgressDialog
import kotlin.Throws
import android.widget.Toast
import com.google.android.gms.tasks.OnFailureListener
import android.app.Activity
import android.net.Uri
import android.view.View
import androidx.fragment.app.Fragment
import com.example.firebasechatapp.databinding.FragmentLoginBinding
import com.example.firebasechatapp.databinding.FragmentProfileBinding
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.storage.UploadTask
import dagger.hilt.android.AndroidEntryPoint
import de.hdodenhof.circleimageview.CircleImageView
import model.User
import java.util.HashMap

@AndroidEntryPoint
class ProfileFragment : Fragment(R.layout.fragment_profile) {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentProfileBinding.bind(view)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}