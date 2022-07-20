package com.example.firebasechatapp.ui.mediaDisplay

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.firebasechatapp.R
import com.example.firebasechatapp.databinding.FragmentMediaBinding
import com.example.firebasechatapp.ui.app_components.MainActivity
import com.example.firebasechatapp.ui.chat.ChatFragmentDirections
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MediaFragment : Fragment(R.layout.fragment_media) {

    private var _binding: FragmentMediaBinding? = null
    private val binding: FragmentMediaBinding get() = _binding!!
    private val args: MediaFragmentArgs by navArgs()

   /* override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requireActivity().onBackPressedDispatcher.addCallback(object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val action = MediaFragmentDirections.actionMediaFragmentToChatFragment(args.channelId, args.otherUserId)
                findNavController().navigate(action)
            }
        })
    }*/

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentMediaBinding.bind(view)
        (activity as MainActivity).supportActionBar?.hide()


        Glide.with(this)
            .load(args.mediaUri)
            .into(binding.zoomImage)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        (activity as MainActivity).supportActionBar?.show()
        _binding = null
    }
}