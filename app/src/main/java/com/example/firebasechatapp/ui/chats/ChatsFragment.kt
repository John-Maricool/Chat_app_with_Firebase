package com.example.firebasechatapp.ui.chats

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.firebasechatapp.R
import com.example.firebasechatapp.data.adapter.ChatsListAdapter
import com.example.firebasechatapp.data.interfaces.OnListItemClickListener
import com.example.firebasechatapp.databinding.FragmentChatsBinding
import com.example.firebasechatapp.ui.app_components.MainActivity
import com.example.firebasechatapp.utils.EventObserver
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ChatsFragment : Fragment(R.layout.fragment_chats), OnListItemClickListener {

    private var _binding: FragmentChatsBinding? = null
    private val binding get() = _binding!!

    private val model: ChatsViewModel by viewModels()

    @Inject
    lateinit var mAdapter: ChatsListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requireActivity().onBackPressedDispatcher.addCallback(object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                requireActivity().finish()
            }
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentChatsBinding.bind(view)
        binding.chatsRecyclerView.apply {
            setHasFixedSize(true)
        }

        binding.adapter = mAdapter
        binding.viewmodel = model
        binding.lifecycleOwner = this.viewLifecycleOwner
        binding.executePendingBindings()
        observeLiveData()
    }

    private fun observeLiveData() {
        model.defaultRepo.dataLoading.observe(viewLifecycleOwner,
            EventObserver { (activity as MainActivity).showGlobalProgressBar(it) })
        model.defaultRepo.snackBarText.observe(viewLifecycleOwner, EventObserver {
            Toast.makeText(activity, it, Toast.LENGTH_SHORT).show()
        })
    }

    override fun onStart() {
        super.onStart()
        mAdapter.setOnItemClickListener(this)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        (activity as MainActivity).showGlobalProgressBar(false)
        _binding = null
    }

    override fun onListItemClick(id: String) {
        model.getChannelId(id)
        model.channelId.observe(viewLifecycleOwner){
            if (it != null){
                val action = ChatsFragmentDirections.actionChatsFragmentToChatFragment(id, it)
                findNavController().navigate(action)
            }
        }

    }
}

