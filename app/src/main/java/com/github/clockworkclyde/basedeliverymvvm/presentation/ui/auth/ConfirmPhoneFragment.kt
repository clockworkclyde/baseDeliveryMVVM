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
import com.google.firebase.auth.PhoneAuthProvider
import dagger.hilt.android.AndroidEntryPoint

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
            viewModel.millisUntilFinished.observe(viewLifecycleOwner) { millis ->
                val text =
                    getString(R.string.time_until_resend_text).format((millis % 100).toString())
                secondsUntilFinishedTextView.text = text
            }
            editText.doOnQueryTextChanged { code ->
                if (code.length == 6) {
                    val credential =
                        PhoneAuthProvider.getCredential(viewModel.verificationId.value!!, code)
                    viewModel.signInWithCredential(credential)
                }
            }
        }

        viewModel.retryButtonIsAvailable.observe(viewLifecycleOwner) { isAvailable ->
            binding.button.isEnabled = isAvailable
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
}