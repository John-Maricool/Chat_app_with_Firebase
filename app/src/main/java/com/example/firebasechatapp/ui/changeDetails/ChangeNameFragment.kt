package com.example.firebasechatapp.ui.changeDetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.example.firebasechatapp.R
import com.example.firebasechatapp.databinding.FragmentChangeNameBinding
import com.example.firebasechatapp.ui.app_components.MainActivity
import com.example.firebasechatapp.utils.EventObserver
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChangeNameFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentChangeNameBinding? = null
    private val binding get() = _binding!!
    private val model: ChangeNameViewModel by viewModels()
    private val args: ChangeNameFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_change_name, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentChangeNameBinding.bind(view)
        model.text.value = args.userInfo.displayName
        model.email.value = args.userInfo.email

        binding.model = model
        binding.executePendingBindings()

        observeLiveData()
    }

    private fun observeLiveData() {
        model.done.observe(viewLifecycleOwner) {
            if (it != null)
                Toast.makeText(activity, it, Toast.LENGTH_SHORT).show()
            dialog?.dismiss()
        }
        model.defaultRepo.snackBarText.observe(viewLifecycleOwner, EventObserver {
            Toast.makeText(activity, it, Toast.LENGTH_SHORT).show()
        })
        model.defaultRepo.dataLoading.observe(viewLifecycleOwner, EventObserver {
            (activity as MainActivity).showGlobalProgressBar(it)
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

