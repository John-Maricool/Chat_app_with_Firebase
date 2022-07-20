package com.example.firebasechatapp.ui.chat

import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentMediaDisplayBinding.bind(view)
        (activity as MainActivity).supportActionBar?.hide()

        binding.image.visibility = View.GONE
        model.channelId = args.channelId!!
        model.otherUserId = args.receiverId!!

        val bitmap =
            BitmapFactory.decodeStream(requireActivity().contentResolver.openInputStream(args.uri!!.toUri()))
        binding.cropImageView.imageBitmap = bitmap
        binding.fragment = this
        binding.model = model
        binding.executePendingBindings()
        observeLiveData()

        binding.send.setOnClickListener {
            model.media = binding.cropImageView.croppedBitmap
            model.sendMediaToStorage()
        }
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