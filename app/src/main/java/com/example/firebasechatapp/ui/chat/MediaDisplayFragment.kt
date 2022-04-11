package com.example.firebasechatapp.ui.chat

import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.MediaController
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.firebasechatapp.R
import com.example.firebasechatapp.databinding.FragmentMediaDisplayBinding
import com.example.firebasechatapp.ui.app_components.MainActivity
import com.example.firebasechatapp.utils.EventObserver
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MediaDisplayFragment : Fragment(R.layout.fragment_media_display) {

    private var _binding: FragmentMediaDisplayBinding? = null
    private val binding: FragmentMediaDisplayBinding get() = _binding!!
    private val args: MediaDisplayFragmentArgs by navArgs()
    private val model: MediaDisplayViewModel by viewModels()

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentMediaDisplayBinding.bind(view)
        model.channelId = args.channelId
        model.type = args.type
        model.media = args.uri
        model.otherUserId = args.receiverId

        if (args.type == "video") {
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

        binding.fragment = this
        binding.model = model
        binding.executePendingBindings()
        observeLiveData()
    }

    private fun observeLiveData() {
        model.done.observe(viewLifecycleOwner, EventObserver{
            activity?.onBackPressed()
        })
        model.defaultRepo.dataLoading.observe(viewLifecycleOwner, EventObserver{
            (activity as MainActivity).showGlobalProgressBar(it)
        })
        model.defaultRepo.snackBarText.observe(viewLifecycleOwner, EventObserver{
            Toast.makeText(activity, it, Toast.LENGTH_SHORT).show()
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        (activity as MainActivity).toolbar.visibility = View.VISIBLE
        _binding = null
    }
}