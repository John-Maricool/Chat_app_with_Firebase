package com.example.firebasechatapp.ui.users

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.firebasechatapp.R
import com.example.firebasechatapp.data.adapter.UsersListAdapter
import com.example.firebasechatapp.data.interfaces.OnListItemClickListener
import com.example.firebasechatapp.databinding.FragmentUsersBinding
import com.example.firebasechatapp.ui.app_components.MainActivity
import com.example.firebasechatapp.utils.EventObserver
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class UsersFragment : Fragment(R.layout.fragment_users), OnListItemClickListener {
    private var _binding: FragmentUsersBinding? = null
    private val binding get() = _binding!!

    private val model: UsersViewModel by viewModels()

    @Inject
    lateinit var mAdapter: UsersListAdapter

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
        _binding = FragmentUsersBinding.bind(view)

        binding.recyclerView.apply {
            setHasFixedSize(true)
        }

        binding.adapter = mAdapter
        binding.viewModel = model
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
        model.isUserAdded(id)
        model.added.observe(viewLifecycleOwner) {
            if (it != null) {
                if (it) {
                    Toast.makeText(
                        activity,
                        "You have already started chatting with this user",
                        Toast.LENGTH_SHORT
                    ).show()
                    return@observe
                } else {
                    val action = UsersFragmentDirections.actionUsersFragmentToMessageFragment(id)
                    findNavController().navigate(action)
                }
            }
        }
    }
}

