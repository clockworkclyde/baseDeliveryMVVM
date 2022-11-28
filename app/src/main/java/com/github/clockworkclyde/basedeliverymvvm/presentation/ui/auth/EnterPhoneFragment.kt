package com.github.clockworkclyde.basedeliverymvvm.presentation.ui.auth

import android.os.Bundle
import android.telephony.PhoneNumberFormattingTextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.github.clockworkclyde.basedeliverymvvm.R
import com.github.clockworkclyde.basedeliverymvvm.databinding.FragmentEnterPhoneBinding
import com.github.clockworkclyde.basedeliverymvvm.presentation.ui.base.BaseFragment
import com.github.clockworkclyde.basedeliverymvvm.util.getFocusAndShowSoftInput
import com.github.clockworkclyde.basedeliverymvvm.util.getTextWithoutDashesAndSpaces
import com.github.clockworkclyde.basedeliverymvvm.util.matchesNumbersOnly
import com.github.clockworkclyde.basedeliverymvvm.util.onSingleClick
import com.github.clockworkclyde.models.local.auth.TimeCounterPref
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import java.util.*
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class EnterPhoneFragment : BaseFragment(R.layout.fragment_enter_phone) {

    override var bottomNavigationViewVisibility: Int = View.GONE
    private val viewModel: AuthViewModel by activityViewModels()
    private val args: EnterPhoneFragmentArgs by navArgs()

    private lateinit var binding: FragmentEnterPhoneBinding
    private lateinit var phone: String

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel.getAuthState().observe(viewLifecycleOwner) { isAuthenticated ->
            if (isAuthenticated) {
                findNavController().popBackStack()
            }
        }
        binding = FragmentEnterPhoneBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            headlineTextView.text = getString(args.stringRes)
            phoneEditText.addTextChangedListener(PhoneNumberFormattingTextWatcher())
            verifyBtn.onSingleClick { checkValidForResult(phoneEditText.getTextWithoutDashesAndSpaces()) }
        }
    }

    private fun tryToVerifyPhoneNumber(phone: String) {
        this.phone = getSelectedCountryCodePrefix() + phone
        verifyPhoneNumberWithOptions(this.phone)
        showProgress()
    }

    private fun getSelectedCountryCodePrefix(): String {
        return "+" + binding.phonePrefixTextView.selectedCountryCode
    }

    private fun showProgress() {
        binding.apply {
            progressBar.isVisible = true
            container.isVisible = false
            verifyBtn.isVisible = false
        }
    }

    private fun verifyPhoneNumberWithOptions(phone: String) {
        val options = PhoneAuthOptions.newBuilder(Firebase.auth)
            .setPhoneNumber(phone)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(requireActivity())
            .setCallbacks(stateCallbacks)
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    private fun checkValidForResult(phone: String) {
        if (phone.matchesNumbersOnly() && phone.length == 10) {
            if (isNotTimeout()) navigateToConfirmPhoneFragment()
            else tryToVerifyPhoneNumber(phone)
        } else if (phone.isEmpty()) {
            showError(R.string.empty_phone_field)
        } else {
            showError(R.string.invalid_symbols_phone_field)
        }
    }

    private val stateCallbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        override fun onVerificationCompleted(credential: PhoneAuthCredential) {
            /** for cases with auto-verification **/
            viewModel.signInWithCredential(credential)
        }

        override fun onVerificationFailed(e: FirebaseException) {
            Timber.e(e)
            when (e) {
                is FirebaseNetworkException -> showError("Error. Please check your internet connection")
                else -> showError(e.toString())
            }
        }

        override fun onCodeSent(id: String, token: PhoneAuthProvider.ForceResendingToken) {
            super.onCodeSent(id, token)
            viewModel.setPhone(phone)
            viewModel.setVerificationId(id)
            TimeCounterPref.millis = Calendar.getInstance().timeInMillis
            navigateToConfirmPhoneFragment()
        }
    }

    private fun navigateToConfirmPhoneFragment() {
        findNavController().navigate(
            EnterPhoneFragmentDirections
                .actionUserSignInFragmentToUserVerificationFragment(args.destinationId)
        )
    }

    private fun showError(id: Int) {
        Toast.makeText(requireContext(), getString(id), Toast.LENGTH_SHORT).show()
    }

    private fun showError(text: String) {
        Toast.makeText(requireContext(), text, Toast.LENGTH_SHORT).show()
    }

    override fun onResume() {
        super.onResume()
        binding.apply {
            if (phoneEditText.isVisible) phoneEditText.getFocusAndShowSoftInput()
        }
    }

    private fun isNotTimeout() =
        Calendar.getInstance().timeInMillis < TimeCounterPref.millis + 60000L

}