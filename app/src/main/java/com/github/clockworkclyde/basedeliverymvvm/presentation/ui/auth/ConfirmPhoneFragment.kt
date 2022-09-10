package com.github.clockworkclyde.basedeliverymvvm.presentation.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.github.clockworkclyde.basedeliverymvvm.R
import com.github.clockworkclyde.basedeliverymvvm.data.repository.Response
import com.github.clockworkclyde.basedeliverymvvm.databinding.FragmentConfirmPhoneBinding
import com.github.clockworkclyde.basedeliverymvvm.presentation.ui.base.BaseFragment
import com.github.clockworkclyde.basedeliverymvvm.presentation.util.doOnQueryTextChanged
import com.github.clockworkclyde.basedeliverymvvm.presentation.util.onSingleClick
import com.github.clockworkclyde.models.local.auth.TimeCounterPref
import com.google.firebase.auth.PhoneAuthProvider
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class ConfirmPhoneFragment : BaseFragment(R.layout.fragment_confirm_phone) {

    override var bottomNavigationViewVisibility: Int = View.GONE

    private val viewModel: AuthViewModel by activityViewModels()
    private val args: ConfirmPhoneFragmentArgs by navArgs()
    private lateinit var binding: FragmentConfirmPhoneBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentConfirmPhoneBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            button.isEnabled = false
            editText.doOnQueryTextChanged { code ->
                if (code.length == 6) {
                    val credential =
                        PhoneAuthProvider.getCredential(viewModel.verificationId.value!!, code)
                    viewModel.signInWithCredential(credential)
                }
            }
        }

        viewModel.signInState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is Response.Error -> showError(state.error)
                is Response.Loading -> showProgress()
                is Response.Success -> {
                    viewModel.saveUser(state.data)
                    navigateToInitializerFragment()
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        val diff = (Calendar.getInstance().timeInMillis - TimeCounterPref.millis)
        if (diff < DEFAULT_TIMEOUT_MS) {
            observeTimeCounterUntilShowRetryButton(DEFAULT_TIMEOUT_MS - diff)
        } else showRetryButton()
    }

    private fun observeTimeCounterUntilShowRetryButton(
        millisInFuture: Long,
        interval: Long = DEFAULT_INTERVAL_MS
    ) {
        viewModel.countDownTimer(millisInFuture, interval)
            .observe(viewLifecycleOwner) { millisUntilFinished ->
                if (millisUntilFinished <= 0) {
                    showRetryButton()
                    return@observe
                }
                val str = millisUntilFinished.toString()
                binding.secondsUntilFinishedTextView.text =
                    getString(R.string.time_until_resend_text).format(
                        str.substring(0, str.length - 3)
                    )
            }
    }

    private fun showRetryButton() {
        binding.secondsUntilFinishedTextView.text = ""
        binding.button.run {
            isEnabled = true
            onSingleClick { Toast.makeText(requireContext(), "Retry!", Toast.LENGTH_SHORT).show() }
        }
    }

    private fun showError(error: Throwable) {
        Toast.makeText(requireContext(), error.toString(), Toast.LENGTH_LONG).show()
    }

    private fun showProgress() {
        binding.editText.isEnabled = false
    }

    private fun navigateToInitializerFragment() {
        val navOptions = NavOptions.Builder()
            .setPopUpTo(args.destinationId, true)
            .build()
        findNavController().navigate(args.destinationId, null, navOptions)
    }

    companion object {
        private const val DEFAULT_TIMEOUT_MS = 60000L
        private const val DEFAULT_INTERVAL_MS = 1000L
    }
}