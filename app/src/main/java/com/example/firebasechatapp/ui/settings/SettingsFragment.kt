package com.example.firebasechatapp.ui.settings

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.firebasechatapp.R
import com.example.firebasechatapp.data.models.UserInfo
import com.example.firebasechatapp.databinding.FragmentSettingsBinding
import com.example.firebasechatapp.ui.app_components.MainActivity
import com.example.firebasechatapp.utils.EventObserver
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingsFragment : Fragment(R.layout.fragment_settings) {

    private var _binding: FragmentSettingsBinding? = null
    private val binding: FragmentSettingsBinding get() = _binding!!
    private val model: SettingsViewModel by viewModels()
    lateinit var user: UserInfo

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
        _binding = FragmentSettingsBinding.bind(view)
        binding.viewmodel = model
        binding.lifecycleOwner = this.viewLifecycleOwner
        binding.executePendingBindings()
        observeLiveData()
    }

    private fun observeLiveData(){
        model.result.observe(viewLifecycleOwner) {
            if (it != null) {
                binding.user = it
                user = it
            }
        }
        model.defaultRepo.dataLoading.observe(viewLifecycleOwner, EventObserver {
            (activity as MainActivity).showGlobalProgressBar(it)
        })
        model.defaultRepo.snackBarText.observe(viewLifecycleOwner, EventObserver {
            Toast.makeText(activity, it, Toast.LENGTH_SHORT).show()
        })
        model.isSignOut.observe(viewLifecycleOwner) {
            if (it != null) {
                findNavController().navigate(R.id.firstFragment)
                findNavController().popBackStack()
            }
        }
        model.navigateToChangeImage.observe(viewLifecycleOwner) {
            if (it != null && this::user.isInitialized) {
                val action =
                    SettingsFragmentDirections.actionSettingsFragmentToProfileFragment()
                findNavController().navigate(action)
            }
        }

        model.activateMode.observe(viewLifecycleOwner, EventObserver{
            if (it){
                AppCompatDelegate
                    .setDefaultNightMode(
                        AppCompatDelegate
                            .MODE_NIGHT_NO)
            }else{
                AppCompatDelegate
                    .setDefaultNightMode(
                        AppCompatDelegate
                            .MODE_NIGHT_YES)
            }
        })
        model.navigateToChangeName.observe(viewLifecycleOwner, EventObserver {
            if (it && this::user.isInitialized) {
                val action =
                    SettingsFragmentDirections.actionSettingsFragmentToChangeNameFragment(user)
                findNavController().navigate(action)
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}