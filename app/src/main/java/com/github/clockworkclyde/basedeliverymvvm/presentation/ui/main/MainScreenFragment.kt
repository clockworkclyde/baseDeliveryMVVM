package com.github.clockworkclyde.basedeliverymvvm.presentation.ui.main

import android.os.Bundle
import android.view.*
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.github.clockworkclyde.basedeliverymvvm.R
import com.github.clockworkclyde.basedeliverymvvm.databinding.FragmentMainBinding
import com.github.clockworkclyde.basedeliverymvvm.presentation.ui.base.BaseFragment
import com.github.clockworkclyde.basedeliverymvvm.presentation.ui.base.MainScreenDelegates
import com.github.clockworkclyde.basedeliverymvvm.presentation.ui.base.model.menu.MenuCategoryItem
import com.github.clockworkclyde.basedeliverymvvm.presentation.ui.base.model.menu.MenuItem
import com.github.clockworkclyde.basedeliverymvvm.presentation.ui.base.ListMediator
import com.github.clockworkclyde.basedeliverymvvm.presentation.vm.main.MainScreenViewModel
import com.google.android.material.tabs.TabLayout
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainScreenFragment : BaseFragment(R.layout.fragment_main) {

    override var bottomNavigationViewVisibility: Int = View.VISIBLE

    private lateinit var binding: FragmentMainBinding
    private val adapter by lazy { MainScreenAdapter(::onItemClick) }
    private val viewModel: MainScreenViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel.errorData.addOnExceptionListener { showError() }
        binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)

        with(binding) {
            recyclerView.adapter = adapter
            recyclerView.setHasFixedSize(true)
            val mediator = ListMediator(recyclerView, tabLayout)

            viewLifecycleOwner.lifecycleScope.launch {
                viewModel.data.collect {
                    tabLayout.setTabs(it)
                    adapter.items = it
                    mediator.updateWithAnchors(it.indices.toList())
                    mediator.attach()
                }
            }

            viewLifecycleOwner.lifecycleScope.launchWhenStarted {
                val navController = findNavController()
                navController.currentBackStackEntryFlow.map { entry ->
                    entry.savedStateHandle.get<MenuItem>("item")
                }
                    .collectLatest { item ->
                        if (item != null) {
                            onItemClick(item, MainScreenDelegates.ClickAction.AddToCart)
                        }
                    }
            }
        }
    }

    private fun TabLayout.setTabs(categories: List<MenuCategoryItem>) {
        removeAllTabs()
        for (category in categories) {
            addTab(newTab().setText(category.title))
        }
        if (categories.size > 3) tabMode = TabLayout.MODE_SCROLLABLE
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

    private fun showError() {
        binding.apply {
            errorBody.isVisible = true
            recyclerView.isVisible = false
            retryButton.setOnClickListener { retryAgain() }
        }
    }

    private fun retryAgain() {
        binding.apply {
            viewModel.initData()
            errorBody.isVisible = false
            recyclerView.isVisible = true
        }
    }

    private fun onItemClick(
        item: MenuItem,
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
}
