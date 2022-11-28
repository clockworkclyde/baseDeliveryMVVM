package com.github.clockworkclyde.basedeliverymvvm.presentation.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.github.clockworkclyde.basedeliverymvvm.R
import com.github.clockworkclyde.basedeliverymvvm.databinding.FragmentProfileBinding
import com.github.clockworkclyde.basedeliverymvvm.presentation.ui.auth.AuthViewModel
import com.github.clockworkclyde.basedeliverymvvm.presentation.ui.base.BaseFragment
import com.github.clockworkclyde.basedeliverymvvm.util.onSingleClick
import com.github.clockworkclyde.models.local.auth.User
import com.github.clockworkclyde.models.remote.base.Response
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment : BaseFragment(R.layout.fragment_profile) {

    private lateinit var binding: FragmentProfileBinding

    private val viewModel: AuthViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getAuthState().observe(viewLifecycleOwner) { isAuthenticated ->
            if (isAuthenticated) {
                initLogOutButton()
            } else initSignInButton()
        }
    }

    private fun initSignInButton() {
        updateUserData(getString(R.string.username_guest))
        with(binding) {
            signInLayout.isVisible = true
            signInButton.onSingleClick { navigateToSignInFragment() }
        }
    }

    private fun initLogOutButton() {
        with(binding) {
            profileButtonsLayout.isVisible = true
            getCurrentUserData()
            logOutButton.onSingleClick {
                viewModel.signOut().observe(viewLifecycleOwner) { isSuccessfully ->
                    if (isSuccessfully) {
                        viewModel.clearVerificationData()
                        profileButtonsLayout.isVisible = false
                        initSignInButton()
                    }
                }
            }
        }
    }

    private fun getCurrentUserData() {
        viewModel.getCurrentUser().observe(viewLifecycleOwner) { response ->
            when (response) {
                is Response.Error -> showConnectionError()
                is Response.Success -> {
                    val user = response.data
                    updateUserData(user.name, user.phone)
                }
            }
        }
    }

    private fun updateUserData(username: String, phone: String = "", birthday: String = "") {
        binding.apply {
            userNameTextView.text = username
        }
    }

    private fun showConnectionError() {
        binding.apply {
            contentLayout.isVisible = false
            connectionErrorTextView.isVisible = true
        }
    }

    private fun retryRequest() {
        Toast.makeText(requireContext(), "Retry", Toast.LENGTH_SHORT).show()
        binding.apply {
            contentLayout.isVisible = true
            binding.connectionErrorTextView.isVisible = false
        }
    }

    private fun navigateToSignInFragment() {
        val navController = findNavController()
        navController.navigate(
            ProfileFragmentDirections.actionProfileFragmentToEnterPhoneFragment(
                R.string.login_for_profile, navController.currentDestination!!.id
            )
        )
    }
}