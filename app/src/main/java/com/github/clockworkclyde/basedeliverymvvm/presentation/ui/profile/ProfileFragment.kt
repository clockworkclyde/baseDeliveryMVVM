package com.github.clockworkclyde.basedeliverymvvm.presentation.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.github.clockworkclyde.basedeliverymvvm.R
import com.github.clockworkclyde.basedeliverymvvm.databinding.FragmentProfileBinding
import com.github.clockworkclyde.basedeliverymvvm.presentation.ui.auth.AuthViewModel
import com.github.clockworkclyde.basedeliverymvvm.presentation.ui.base.BaseFragment
import com.github.clockworkclyde.basedeliverymvvm.presentation.util.onSingleClick
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
        binding.signInButton.apply {
            isVisible = true
            onSingleClick { navigateToSignInFragment() }
        }
    }

    private fun initLogOutButton() {
        binding.logOutButton.apply {
            isVisible = true
            onSingleClick {
                viewModel.signOut().observe(viewLifecycleOwner) { isSuccessfully ->
                    if (isSuccessfully) {
                        isVisible = false
                        initSignInButton()
                    }
                }
            }
        }
    }

    private fun navigateToSignInFragment() =
        findNavController().navigate(
            ProfileFragmentDirections.actionProfileFragmentToEnterPhoneFragment(
                R.string.login_for_profile
            )
        )
}