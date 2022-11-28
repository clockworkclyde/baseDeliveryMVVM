package com.github.clockworkclyde.basedeliverymvvm.presentation.ui.order

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import com.github.clockworkclyde.basedeliverymvvm.R
import com.github.clockworkclyde.basedeliverymvvm.databinding.FragmentEditOrderPhoneBinding
import com.github.clockworkclyde.basedeliverymvvm.presentation.ui.base.BaseDialogFragment
import com.github.clockworkclyde.basedeliverymvvm.util.getFocusAndShowSoftInput
import com.github.clockworkclyde.basedeliverymvvm.util.getTextWithoutDashesAndSpaces
import com.github.clockworkclyde.basedeliverymvvm.util.matchesNumbersOnly
import com.github.clockworkclyde.basedeliverymvvm.util.onSingleClick

class EditOrderPhoneFragment : BaseDialogFragment() {

    private lateinit var binding: FragmentEditOrderPhoneBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEditOrderPhoneBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            button.onSingleClick {
                getFormattedPhoneOrNull()?.let { phone ->
                    putPhoneToBackStack(phone)
                    findNavController().popBackStack()
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        binding.phoneEditText.getFocusAndShowSoftInput()
    }

    private fun getFormattedPhoneOrNull(): String? {
        return with(binding) {
            val phone = phoneEditText.getTextWithoutDashesAndSpaces()
            if (isPhoneValid(phone)) {
                "+" + phonePrefixTextView.selectedCountryCode + phone
            } else null
        }
    }

    private fun putPhoneToBackStack(phone: String) {
        findNavController().previousBackStackEntry?.savedStateHandle?.set("phone", phone)
    }

    private fun isPhoneValid(phone: String): Boolean {
        return if (phone.matchesNumbersOnly() && phone.length == 10) {
            true
        } else if (phone.isEmpty()) {
            showError(R.string.empty_phone_field)
            false
        } else {
            showError(R.string.invalid_symbols_phone_field)
            false
        }
    }

    private fun showError(id: Int) {
        Toast.makeText(requireContext(), getString(id), Toast.LENGTH_SHORT).show()
    }
}