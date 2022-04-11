package com.example.firebasechatapp.ui.firstmessage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.firebasechatapp.R
import com.example.firebasechatapp.databinding.FragmentMessageBinding
import com.example.firebasechatapp.ui.app_components.MainActivity
import com.example.firebasechatapp.utils.EventObserver
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MessageFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentMessageBinding? = null
    private val binding get() = _binding!!

    private val args: MessageFragmentArgs by navArgs()
    private val model: MessageViewModel by viewModels()
    lateinit var secUserId: String

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_message, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentMessageBinding.bind(view)
        secUserId = args.id
        binding.model = model
        binding.frag = this
        binding.executePendingBindings()
        observeLiveData()
    }

    private fun observeLiveData() {
        model.defRepo.dataLoading.observe(viewLifecycleOwner, EventObserver {
            (activity as MainActivity).showGlobalProgressBar(it)
        })
        model.defRepo.snackBarText.observe(viewLifecycleOwner, EventObserver {
            Toast.makeText(activity, it, Toast.LENGTH_SHORT).show()
        })
        model.finished.observe(viewLifecycleOwner) {
            if (it != null) {
                val action =
                    MessageFragmentDirections.actionMessageFragmentToChatFragment(secUserId, it)
                findNavController().navigate(action)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}