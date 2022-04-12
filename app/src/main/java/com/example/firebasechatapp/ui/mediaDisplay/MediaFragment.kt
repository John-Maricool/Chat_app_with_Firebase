package com.example.firebasechatapp.ui.mediaDisplay

import android.os.Bundle
import android.view.View
import android.widget.MediaController
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.firebasechatapp.R
import com.example.firebasechatapp.databinding.FragmentMediaBinding
import com.example.firebasechatapp.utils.Constants
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MediaFragment : DialogFragment(R.layout.fragment_media) {

    private var _binding: FragmentMediaBinding? = null
    private val binding: FragmentMediaBinding get() = _binding!!
    private val args: MediaFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?){
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentMediaBinding.bind(view)
        if (args.type == Constants.TYPE_VIDEO){
            binding.image.visibility = View.GONE
            binding.video.setVideoPath(args.mediaUri)
            val controller = MediaController(requireActivity())
            controller.setAnchorView(binding.video)

            binding.video.setMediaController(controller)
            binding.video.start()
        }else{
            binding.video.visibility = View.GONE
            Glide.with(this)
                .load(args.mediaUri)
                .into(binding.image)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}