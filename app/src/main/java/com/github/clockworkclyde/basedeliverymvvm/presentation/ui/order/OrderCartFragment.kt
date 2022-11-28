package com.github.clockworkclyde.basedeliverymvvm.presentation.ui.order

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.github.clockworkclyde.basedeliverymvvm.R
import com.github.clockworkclyde.basedeliverymvvm.databinding.FragmentCartBinding
import com.github.clockworkclyde.basedeliverymvvm.presentation.ui.auth.AuthViewModel
import com.github.clockworkclyde.basedeliverymvvm.presentation.ui.base.BaseFragment
import com.github.clockworkclyde.basedeliverymvvm.util.clearList
import com.github.clockworkclyde.basedeliverymvvm.util.onSingleClick
import com.github.clockworkclyde.basedeliverymvvm.util.setList
import com.github.clockworkclyde.models.ui.base.ViewState
import com.github.clockworkclyde.models.ui.dishes.DishItem
import com.github.clockworkclyde.models.ui.order.DishExtraEntity
import com.google.android.material.transition.MaterialFadeThrough
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OrderCartFragment : BaseFragment(R.layout.fragment_cart), OnOrderItemClickListener {

    override var bottomNavigationViewVisibility: Int = View.VISIBLE

    private lateinit var binding: FragmentCartBinding
    private val checkoutViewModel: CheckoutViewModel by activityViewModels()
    private val authViewModel: AuthViewModel by activityViewModels()
    private val orderViewModel: OrderCartViewModel by viewModels()
    private val adapter by lazy { OrderCartAdapter(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enterTransition = MaterialFadeThrough().apply {
            duration = resources.getInteger(R.integer.motion_duration_large).toLong()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCartBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            recyclerView.adapter = adapter
            checkoutViewModel.viewState.observe(viewLifecycleOwner) { state ->
                when (state) {
                    is ViewState.Empty -> {
                        adapter.clearList()
                    }
                    is ViewState.Error -> {}
                    is ViewState.Loading -> {}
                    is ViewState.Success -> {
                        adapter.setList(state.data)
                    }
                }
            }
            checkoutViewModel.totalPrice.observe(viewLifecycleOwner) { total ->
                if (total > 0) {
                    createOrderButton.text = "Оформить заказ за $total ₽."
                }
            }
            checkoutViewModel.nextButtonVisibility.observe(viewLifecycleOwner) { isVisible ->
                setBottomLayoutVisibility(isVisible)
            }
            createOrderButton.onSingleClick {
                authViewModel.getAuthState().observe(viewLifecycleOwner) { isAuthenticated ->
                    if (!isAuthenticated) { // !
                        navigateToCheckout()
                    } else {
                        checkoutViewModel.resetOrderDetails()
                        navigateToSignInFragment()
                    }
                }
            }
        }
    }

    private fun setBottomLayoutVisibility(isVisible: Boolean = false) {
        binding.buttonLayout.isVisible = isVisible
    }

    override fun onButtonClick(
        id: Long,
        action: QuantityButtonAction,
        extras: List<DishExtraEntity>,
        additionTime: Long
    ) {
        orderViewModel.updateItemByAction(id, action, extras, additionTime)
    }

    override fun onItemClick(item: DishItem, extras: List<DishExtraEntity>, quantity: Int) {
        findNavController().navigate(
            OrderCartFragmentDirections.actionToDetailsFragment(
                dishItem = item,
                extrasList = extras.toTypedArray()
            )
        )
    }

    private fun navigateToSignInFragment() {
        val navController = findNavController()
        navController.navigate(
            OrderCartFragmentDirections.actionOrderCartFragmentToEnterPhoneFragment(
                R.string.login_for_order, navController.currentDestination!!.id
            )
        )
    }

    private fun navigateToCheckout() {
        findNavController().navigate(OrderCartFragmentDirections.actionOrderCartFragmentToCheckoutFragment())
    }
}