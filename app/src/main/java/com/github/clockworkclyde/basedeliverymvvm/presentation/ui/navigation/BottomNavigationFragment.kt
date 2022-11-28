package com.github.clockworkclyde.basedeliverymvvm.presentation.ui.navigation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavController
import androidx.navigation.ui.setupWithNavController
import com.github.clockworkclyde.basedeliverymvvm.R
import com.github.clockworkclyde.basedeliverymvvm.databinding.FragmentBottomNavigationBinding
import com.github.clockworkclyde.basedeliverymvvm.presentation.ui.MainActivity
import com.github.clockworkclyde.basedeliverymvvm.presentation.ui.order.CheckoutViewModel
import com.github.clockworkclyde.models.ui.base.ViewState

class BottomNavigationFragment : Fragment(R.layout.fragment_bottom_navigation) {

    private lateinit var binding: FragmentBottomNavigationBinding
    private var navController: NavController? = null

    private val checkoutViewModel: CheckoutViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        initNavHostNavController()
        binding = FragmentBottomNavigationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.root.setupWithNavController(navController!!)
        observeOrderCartCount()
    }

    private fun initNavHostNavController() {
        navController = (activity as MainActivity?)?.getNavHostFragmentNavController()
    }

    private fun observeOrderCartCount() {
        checkoutViewModel.viewState.observe(viewLifecycleOwner) { state ->
            binding.root.getOrCreateBadge(R.id.orderCartFragment).apply {
                if (state is ViewState.Success) {
                    number = state.data.count().also {
                        isVisible = it > 0
                    }
                } else {
                    isVisible = false
                }
            }
        }
    }
}