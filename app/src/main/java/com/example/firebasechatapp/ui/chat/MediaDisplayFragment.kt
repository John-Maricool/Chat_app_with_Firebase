package com.example.firebasechatapp.ui.chat

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.MediaController
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.firebasechatapp.R
import com.example.firebasechatapp.databinding.FragmentMediaDisplayBinding
import com.example.firebasechatapp.ui.app_components.MainActivity
import com.example.firebasechatapp.utils.Constants
import com.example.firebasechatapp.utils.EventObserver
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MediaDisplayFragment : Fragment(R.layout.fragment_media_display) {

    private var _binding: FragmentMediaDisplayBinding? = null
    private val binding: FragmentMediaDisplayBinding get() = _binding!!
    private val args: MediaDisplayFragmentArgs by navArgs()
    private val model: MediaDisplayViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentMediaDisplayBinding.bind(view)
        (activity as MainActivity).supportActionBar?.hide()

        if (args.savedMedia == null) {
            model.channelId = args.channelId!!
            model.type = args.type
            model.media = args.uri!!
            model.otherUserId = args.receiverId!!

            if (args.type == Constants.TYPE_VIDEO) {
                binding.image.visibility = View.GONE
                val v = Uri.parse(args.uri)
                binding.videoPlayer.setVideoURI(v)
                val controller = MediaController(requireActivity())
                controller.setAnchorView(binding.videoPlayer)
                binding.videoPlayer.setMediaController(controller)
                binding.videoPlayer.start()
            } else {
                binding.videoPlayer.visibility = View.GONE
                Glide.with(this)
                    .load(args.uri)
                    .into(binding.image)
            }
        } else {
            binding.send.visibility = View.GONE
            if (args.savedMedia!!.type == Constants.TYPE_IMAGE) {
                binding.videoPlayer.visibility = View.GONE
                Glide.with(this)
                    .load(args.savedMedia!!.media)
                    .into(binding.image)
            } else {
                binding.image.visibility = View.GONE
                val v = Uri.parse(args.savedMedia?.path)
                binding.videoPlayer.setVideoURI(v)
                val controller = MediaController(requireActivity())
                controller.setAnchorView(binding.videoPlayer)
                binding.videoPlayer.setMediaController(controller)
                binding.videoPlayer.start()
            }
        }

        binding.fragment = this
        binding.model = model
        binding.executePendingBindings()
        observeLiveData()
    }

    private fun observeLiveData() {
        model.done.observe(viewLifecycleOwner, EventObserver {
            if (it)
                activity?.onBackPressed()
        })
        model.defaultRepo.dataLoading.observe(viewLifecycleOwner, EventObserver {
            (activity as MainActivity).showGlobalProgressBar(it)
        })
        model.defaultRepo.snackBarText.observe(viewLifecycleOwner, EventObserver {
            Toast.makeText(activity, it, Toast.LENGTH_SHORT).show()
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        (activity as MainActivity).showGlobalProgressBar(false)
        (activity as MainActivity).supportActionBar?.show()
        _binding = null
    }
}