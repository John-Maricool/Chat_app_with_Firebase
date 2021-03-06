package com.example.firebasechatapp.ui.saved_media_list

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.firebasechatapp.R
import com.example.firebasechatapp.data.interfaces.OnSavedMediaItemClickedListener
import com.example.firebasechatapp.databinding.FragmentSavedMediaBinding
import com.example.firebasechatapp.ui.app_components.MainActivity
import com.example.firebasechatapp.utils.EventObserver
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SavedMediaFragment : Fragment(R.layout.fragment_saved_media),
    OnSavedMediaItemClickedListener {

    private var _binding: FragmentSavedMediaBinding? = null
    private val binding get() = _binding!!
    private val model: SavedMediaViewModel by viewModels()
    private val args: SavedMediaFragmentArgs by navArgs()

    @Inject
    lateinit var adapter: SavedMediaAdapter

    /* override fun onCreate(savedInstanceState: Bundle?) {
         super.onCreate(savedInstanceState)
         requireActivity().onBackPressedDispatcher.addCallback(object : OnBackPressedCallback(true) {
             override fun handleOnBackPressed() {
                 findNavController().popBackStack(R.id.chatFragment, true)
             }
         })
     }*/

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentSavedMediaBinding.bind(view)
        model.getStoredMedia(args.channelId)
        binding.adapter = adapter
        binding.model = model
        binding.lifecycleOwner = this.viewLifecycleOwner
        binding.executePendingBindings()
        observeLiveData()
        adapter.setOnItemClickListener(this)
    }

    private fun observeLiveData() {
        model.defaultRepo.dataLoading.observe(viewLifecycleOwner, EventObserver {
            (activity as MainActivity).showGlobalProgressBar(it)
        })
        model.defaultRepo.snackBarText.observe(viewLifecycleOwner, EventObserver {
            Toast.makeText(activity, it, Toast.LENGTH_SHORT).show()
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onSavedItemClicked(media: String) {
        val action = SavedMediaFragmentDirections.actionSavedMediaFragmentToMediaFragment(
            media
        )
        findNavController().navigate(action)
    }
}
















