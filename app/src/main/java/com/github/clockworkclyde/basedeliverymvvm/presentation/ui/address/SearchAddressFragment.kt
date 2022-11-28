package com.github.clockworkclyde.basedeliverymvvm.presentation.ui.address

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.github.clockworkclyde.basedeliverymvvm.R
import com.github.clockworkclyde.basedeliverymvvm.databinding.FragmentSearchAddressBinding
import com.github.clockworkclyde.basedeliverymvvm.presentation.ui.base.BaseFragment
import com.github.clockworkclyde.basedeliverymvvm.presentation.ui.order.CheckoutFragmentDirections
import com.github.clockworkclyde.basedeliverymvvm.presentation.ui.order.CheckoutViewModel
import com.github.clockworkclyde.basedeliverymvvm.util.doOnQueryTextChanged
import com.github.clockworkclyde.basedeliverymvvm.util.launchWhenResumed
import com.github.clockworkclyde.models.ui.base.ViewState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class SearchAddressFragment : BaseFragment(R.layout.fragment_search_address) {

    override var bottomNavigationViewVisibility: Int = View.VISIBLE

    private lateinit var binding: FragmentSearchAddressBinding
    private val adapter by lazy {
        AddressSuggestionsAdapter(
            ::onUserAddressClick,
            ::onSearchResultClick
        )
    }
    private val searchAddressViewModel: SearchAddressViewModel by viewModels()
    private val orderViewModel: CheckoutViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchAddressBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        applySearchView()
        initSuggestionsList()
        initSearch()
        observeViewUpdates()
        observeOrderAddress()
    }

    private fun observeViewUpdates() {
        searchAddressViewModel.inputText.observe(viewLifecycleOwner) {
            binding.textInputEditText.setText(it)
        }
    }

    private fun observeOrderAddress() {
        searchAddressViewModel.selectedAddress
            .filterNotNull()
            .onEach {
                orderViewModel.setOrderAddress(it)
            }
            .launchWhenResumed(lifecycleScope)
    }

    private fun applySearchView() {
        with(binding) {
            textInputEditText.apply {
                setOnFocusChangeListener { _, hasFocus ->
                    dividerView.isVisible = hasFocus
                    textView.isVisible = hasFocus
                }
                doOnQueryTextChanged { query ->
                    searchAddressViewModel.updateQuery(query)
                }
                textView.setOnClickListener {
                    navigateToEnterAddressOnMap()
                }
            }

        }
    }

    private fun initSuggestionsList() {
        binding.recyclerView.adapter = adapter
        searchAddressViewModel.viewState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is ViewState.Empty -> {}
                is ViewState.Error -> {}
                is ViewState.Loading -> {
                    adapter.items = listOf()
                }
                is ViewState.Success -> {
                    adapter.items = state.data
                }
            }
        }
    }

    private fun initSearch() {
        lifecycleScope.launchWhenResumed { searchAddressViewModel.collectSearchEvent() }
    }

    private fun onSearchResultClick(index: Int) {
        searchAddressViewModel.getSearchSuggestionData(index)
    }

    private fun onUserAddressClick(index: Int) {
        searchAddressViewModel.onUserAddressClick(index)
    }

    private fun setImageErrorRequest(title: String?) {
        title?.let { Toast.makeText(requireContext(), title, Toast.LENGTH_SHORT).show() }
        //show ErrorRequestImage on list place
    }

    private fun navigateToEnterAddressOnMap() {
        findNavController().navigate(CheckoutFragmentDirections.actionToEnterAddressFragment())
    }
}