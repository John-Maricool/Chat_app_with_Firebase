package com.example.firebasechatapp.ui.chat

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.RecyclerView
import com.example.firebasechatapp.R
import com.example.firebasechatapp.data.adapter.ChatMessagesAdapter
import com.example.firebasechatapp.data.interfaces.OnMediaItemClickListener
import com.example.firebasechatapp.databinding.ChatToolbarBinding
import com.example.firebasechatapp.databinding.FragmentChatBinding
import com.example.firebasechatapp.ui.app_components.MainActivity
import com.example.firebasechatapp.utils.Constants
import com.example.firebasechatapp.utils.EventObserver
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ChatFragment : Fragment(R.layout.fragment_chat), OnMediaItemClickListener {

    private var _binding: FragmentChatBinding? = null
    private val binding get() = _binding!!
    private var _bindingToolbar: ChatToolbarBinding? = null
    private val model: ChatViewModel by viewModels()
    private lateinit var resultLauncher: ActivityResultLauncher<Intent>
    private val args: ChatFragmentArgs by navArgs()

    @Inject
    lateinit var adapter: ChatMessagesAdapter
    private lateinit var listAdapterObserver: RecyclerView.AdapterDataObserver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        resultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    val dataUri = result.data?.data.toString()
                    val type =
                        if (dataUri.contains("video")) Constants.TYPE_VIDEO else Constants.TYPE_IMAGE
                    val action = ChatFragmentDirections.actionChatFragmentToMediaDisplayFragment(
                        type, args.channelId, dataUri, args.otherUserId, null
                    )
                    findNavController().navigate(action)
                }
            }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding =
            FragmentChatBinding.inflate(inflater, container, false)
        _bindingToolbar =
            ChatToolbarBinding.inflate(inflater, container, false)
        setupCustomToolbar()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        model.otherUserId = args.otherUserId
        model.channelId = args.channelId
        model.getUserInfo(args.otherUserId)
        model.getAllMessages()
        adapter.setOnItemClickListener(this)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.adapter = adapter
        binding.model = model
        _bindingToolbar!!.lifecycleOwner = viewLifecycleOwner
        _bindingToolbar!!.viewmodel = model
        binding.executePendingBindings()
        setupListAdapter()
        setClickListeners()
        observeLiveData()
    }

    private fun observeLiveData() {
        model.media.observe(viewLifecycleOwner, EventObserver {
            if (it) {
                val action =
                    ChatFragmentDirections.actionChatFragmentToSavedMediaFragment(args.channelId)
                findNavController().navigate(action)
            }
        })

        model.messages.observe(viewLifecycleOwner) {
            if (it != null) {
                adapter.getMessages(it)
                binding.recyclerView.scrollToPosition(it.size - 1)
            }
        }

        model.olderMessages.observe(viewLifecycleOwner) {
            if (it != null) {
                adapter.addNewMessages(it)
                model.CURRENT_SCROLL_POSITION.value?.let { scr ->
                    binding.recyclerView.scrollToPosition(scr)
                }
            }
        }
    }

    private fun setClickListeners() {
        binding.gallery.setOnClickListener { openGallery() }
        binding.camera.setOnClickListener { openVideo() }
        binding.refresh.setOnRefreshListener {
            model.loadNewPage()
            binding.refresh.isRefreshing = false
        }
    }

    private fun setupCustomToolbar() {
        val supportActionBar = (activity as MainActivity).supportActionBar
        supportActionBar?.setDisplayShowCustomEnabled(true)
        supportActionBar?.customView = _bindingToolbar?.root
    }

    private fun removeCustomToolbar() {
        val supportActionBar = (activity as MainActivity).supportActionBar
        supportActionBar?.setDisplayShowCustomEnabled(false)
        supportActionBar?.customView = null
    }

    private fun setupListAdapter() {
        listAdapterObserver = (object : RecyclerView.AdapterDataObserver() {
            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                binding.recyclerView.scrollToPosition(positionStart)
            }
        })
        adapter.registerAdapterDataObserver(listAdapterObserver)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        removeCustomToolbar()
        adapter.unregisterAdapterDataObserver(listAdapterObserver)
        _binding = null
        _bindingToolbar = null
    }

    private fun openGallery() {
        val intent =
            Intent(Intent.ACTION_OPEN_DOCUMENT, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
        intent.type = "image/*"
        resultLauncher.launch(intent)
    }

    private fun openVideo() {
        val intent =
            Intent(Intent.ACTION_OPEN_DOCUMENT, MediaStore.Video.Media.INTERNAL_CONTENT_URI)
        intent.type = "video/*"
        resultLauncher.launch(intent)
    }

    override fun onMediaItemClick(type: Int, mediaLink: String) {
        val action = ChatFragmentDirections.actionChatFragmentToMediaFragment(
            type,
            mediaLink,
            args.channelId
        )
        findNavController().navigate(action)
    }
}