package com.example.firebasechatapp.ui.users

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.firebasechatapp.R
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
                findNavController().navigate(R.id.chatsFragment)
            }
        })
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentUsersBinding.bind(view)
        setHasOptionsMenu(true)
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

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.search, menu)
        val searchItem = menu.findItem(R.id.search)
        val searchView: SearchView = searchItem.actionView as SearchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                mAdapter.filter.filter(query)
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                mAdapter.filter.filter(newText)
                return false
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("cleared", "removed")
    }
}

