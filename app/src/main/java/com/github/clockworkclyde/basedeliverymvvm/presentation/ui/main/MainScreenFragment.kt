package com.github.clockworkclyde.basedeliverymvvm.presentation.ui.main

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.github.clockworkclyde.basedeliverymvvm.R
import com.github.clockworkclyde.basedeliverymvvm.databinding.FragmentMainBinding
import com.github.clockworkclyde.basedeliverymvvm.presentation.ui.base.BaseFragment
import com.github.clockworkclyde.basedeliverymvvm.presentation.ui.base.MainScreenDelegates
import com.github.clockworkclyde.basedeliverymvvm.presentation.util.ListMediator
import com.github.clockworkclyde.basedeliverymvvm.presentation.vm.main.MainScreenViewModel
import com.github.clockworkclyde.models.ui.menu.DishItem
import com.github.clockworkclyde.models.ui.menu.DishProgress
import com.github.clockworkclyde.models.ui.menu.DishesCategoryItem
import com.github.clockworkclyde.network.api.ViewState
import com.google.android.material.tabs.TabLayout
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainScreenFragment : BaseFragment(R.layout.fragment_main) {

    override var bottomNavigationViewVisibility: Int = View.VISIBLE
    private var errorViewIsVisible = false

    private lateinit var binding: FragmentMainBinding
    private val adapter by lazy { MainScreenAdapter(::onItemClick) }
    private val viewModel: MainScreenViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel.errorData.addOnExceptionListener {
            errorViewIsVisible = true
            showError()
        }
        binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)

        //viewModel.fetchLatestData()

        with(binding) {
            recyclerView.adapter = adapter
            recyclerView.setHasFixedSize(true)
            val mediator = ListMediator(recyclerView, tabLayout)

            viewLifecycleOwner.lifecycleScope.launch {
                viewModel.data.collect { viewState ->
                    val categories: List<DishesCategoryItem> = when (viewState) {
                        is ViewState.Loading -> {
                            getProgressItems()
                        }
                        is ViewState.Success -> {
                            viewState.data
                        }
                        is ViewState.Empty -> {
                            getEmptyItems()
                        }
                        is ViewState.Error -> {
                            getErrorItems()
                        }
                    }
                    tabLayout.setTabs(categories)
                    adapter.items = categories
                    mediator.updateWithAnchors(categories.indices.toList())
                    if (tabLayout.tabCount > 1 && viewState is ViewState.Success) mediator.attach()
                }
            }

            viewLifecycleOwner.lifecycleScope.launchWhenStarted {
                val navController = findNavController()
                navController.currentBackStackEntryFlow.map { entry ->
                    entry.savedStateHandle.get<DishItem>("item")
                }
                    .collectLatest { item ->
                        if (item != null) {
                            onItemClick(item, MainScreenDelegates.ClickAction.AddToCart)
                        }
                    }
            }
        }
    }

    private fun TabLayout.setTabs(categories: List<DishesCategoryItem>) {
        removeAllTabs()
        isVisible = false
        for (category in categories) {
            addTab(newTab().setText(getString(category.categoryId ?: R.string.empty)))
        }
        if (tabCount > 1) isVisible = true
    }

    private fun showError() {
        binding.apply {
            errorBody.isVisible = true
            recyclerView.isVisible = false
            retryButton.setOnClickListener { retryAgain() }
        }
    }

    private fun retryAgain() {
        binding.apply {
            viewModel.fetchLatestData()
            errorViewIsVisible = false
            errorBody.isVisible = false
            recyclerView.isVisible = true
        }
    }

    private fun onItemClick(
        item: DishItem,
        dest: MainScreenDelegates.ClickAction
    ) {
        when (dest) {
            MainScreenDelegates.ClickAction.OpenDetails -> findNavController().navigate(
                MainScreenFragmentDirections.actionToDetailsFragment(item)
            )
            MainScreenDelegates.ClickAction.AddToCart -> viewModel.addToOrderCart(item)
        }
    }

    private fun navigateToSearchFragment() {
        findNavController().navigate(R.id.action_mainScreenFragment_to_searchFragment)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.main_menu, menu)

        val searchButton = menu.findItem(R.id.searchButton)

        searchButton.setOnMenuItemClickListener {
            navigateToSearchFragment()
            true
        }
    }

    private fun getEmptyItems(): List<DishesCategoryItem> {
        Toast.makeText(requireContext(), "Its empty", Toast.LENGTH_SHORT).show()
        return emptyList()
    }

    private fun getProgressItems(): List<DishesCategoryItem> {
        val items = IntRange(1, 10).map { DishProgress }
        return listOf(DishesCategoryItem(null, items))
    }

    private fun getErrorItems(): List<DishesCategoryItem> {
        Toast.makeText(requireContext(), "Error", Toast.LENGTH_SHORT).show()
        return emptyList()
    }
}
