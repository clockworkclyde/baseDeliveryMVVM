package com.github.clockworkclyde.basedeliverymvvm.layers.ui.screens.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.github.clockworkclyde.basedeliverymvvm.databinding.FragmentDetailsBinding
import com.github.clockworkclyde.basedeliverymvvm.layers.ui.navigation.OnBottomSheetCallback
import com.github.clockworkclyde.basedeliverymvvm.layers.ui.screens.MainActivity
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class DetailsFragment : BottomSheetDialogFragment(), OnBottomSheetCallback {

    private var currentState: Int = BottomSheetBehavior.STATE_EXPANDED
    private lateinit var binding: FragmentDetailsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (currentState == BottomSheetBehavior.STATE_EXPANDED) {
            (activity as MainActivity).openBottomSheet()
        } else {
            (activity as MainActivity).hideBottomSheet()
        }
    }

    override fun onStateChanged(bottomSheet: View, newState: Int) {
        currentState = newState
        when (newState) {
            BottomSheetBehavior.STATE_HIDDEN -> {
                binding.apply {
                    Toast.makeText(requireContext(), "IS hidden", Toast.LENGTH_SHORT).show()
                }
            }
            BottomSheetBehavior.STATE_EXPANDED -> {
                binding.apply {
                    Toast.makeText(requireContext(), "is expanded", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}