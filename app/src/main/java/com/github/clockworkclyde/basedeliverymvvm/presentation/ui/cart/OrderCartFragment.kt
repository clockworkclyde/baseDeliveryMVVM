package com.github.clockworkclyde.basedeliverymvvm.presentation.ui.cart

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.github.clockworkclyde.basedeliverymvvm.R
import com.github.clockworkclyde.basedeliverymvvm.databinding.FragmentCartBinding
import com.github.clockworkclyde.basedeliverymvvm.presentation.vm.cart.OrderCartViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OrderCartFragment : Fragment(R.layout.fragment_cart) {

    lateinit var binding: FragmentCartBinding
    private val viewModel: OrderCartViewModel by viewModels()
    private val adapter by lazy { OrderCartAdapter() }

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
                    Log.e("order data flow", it.toString())
                }
            }
        }
    }
}