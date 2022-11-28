package com.github.clockworkclyde.basedeliverymvvm.presentation.ui.order

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.github.clockworkclyde.basedeliverymvvm.R
import com.github.clockworkclyde.basedeliverymvvm.databinding.FragmentOrderDetailsBinding
import com.github.clockworkclyde.basedeliverymvvm.presentation.ui.base.BaseFragment
import com.github.clockworkclyde.basedeliverymvvm.util.launchWhenResumed
import kotlinx.coroutines.flow.onEach

class OrderDetailsFragment : BaseFragment(R.layout.fragment_order_details) {

    private lateinit var binding: FragmentOrderDetailsBinding
    private val orderViewModel: CheckoutViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =  FragmentOrderDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            orderViewModel.orderAddress
                .onEach { orderAddress.text = it.toString() }
                .launchWhenResumed(lifecycleScope)
            orderViewModel.orderPhone.observe(viewLifecycleOwner) {
                orderPhone.text = it
            }
            orderViewModel.orderPaymentId.observe(viewLifecycleOwner) { id ->
                paymentMethod.text = getString(id)
            }
            orderViewModel.totalPrice.observe(viewLifecycleOwner) {
                total.text = it.toString()
            }
        }
    }
}