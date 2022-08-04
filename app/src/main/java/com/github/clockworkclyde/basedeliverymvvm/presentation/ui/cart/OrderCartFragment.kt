package com.github.clockworkclyde.basedeliverymvvm.presentation.ui.cart

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.github.clockworkclyde.basedeliverymvvm.R
import com.github.clockworkclyde.basedeliverymvvm.databinding.FragmentCartBinding
import com.github.clockworkclyde.basedeliverymvvm.presentation.ui.base.BaseFragment
import com.github.clockworkclyde.basedeliverymvvm.presentation.ui.base.model.cart.OrderProductItem
import com.github.clockworkclyde.basedeliverymvvm.presentation.vm.cart.OrderCartViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OrderCartFragment : BaseFragment(R.layout.fragment_cart) {

    override var bottomNavigationViewVisibility: Int = View.VISIBLE

    private lateinit var binding: FragmentCartBinding
    private val viewModel: OrderCartViewModel by viewModels()
    private val adapter by lazy { OrderCartAdapter(::onQuantityButtonClick) }

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
            viewLifecycleOwner.lifecycleScope.launchWhenStarted {
                viewModel.data.collect {
                    adapter.items = it
                    calculateTotalPrice(it)
                }
            }
        }
    }

    private fun calculateTotalPrice(list: List<OrderProductItem>) {
        if (list.isNotEmpty()) {
            binding.createOrderButton.isVisible = true
            val totalPrice = list.sumOf { item -> item.price * item.quantity }
            binding.createOrderButton.text = "Total price is $totalPrice p."
        } else {
            binding.createOrderButton.isVisible = false
        }
    }

    private fun onQuantityButtonClick(id: Long, diff: Int) {
        viewModel.changeItemQuantity(id, diff)
    }
}