package com.github.clockworkclyde.basedeliverymvvm.layers.ui.screens.main

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.github.clockworkclyde.basedeliverymvvm.R
import com.github.clockworkclyde.basedeliverymvvm.databinding.FragmentMainBinding
import com.github.clockworkclyde.basedeliverymvvm.layers.data.base.ListItem
import com.github.clockworkclyde.basedeliverymvvm.layers.ui.util.MainScreenScrollListener
import com.github.clockworkclyde.basedeliverymvvm.layers.ui.vm.main.MainScreenViewModel
import com.google.android.material.tabs.TabLayout
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainScreenFragment : Fragment(R.layout.fragment_main) {

    private lateinit var binding: FragmentMainBinding
    private val adapter by lazy { MainScreenAdapter() }
    private val viewModel: MainScreenViewModel by viewModels()

    companion object {
        const val initialAnchorValue = 0
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val anchors = arrayListOf(initialAnchorValue)
        var contentListSize = 0
        setHasOptionsMenu(true)

        with(binding) {
            recyclerView.adapter = adapter
            recyclerView.setHasFixedSize(true)

            viewLifecycleOwner.lifecycleScope.launch {
                viewModel.data.map { categories ->
                    val contentList = arrayListOf<ListItem>()
                    val categoryTabs = mutableListOf<String>()
                    categories.map {
                        contentList.addAll(it.items)
                        anchors.add(anchors.last().toInt().plus(it.items.size))
                        categoryTabs.add(it.category)
                    }
                    tabLayout.setNewAnchorTabs(categoryTabs)
                    contentList
                }
                    .collect {
                        contentListSize = it.size
                        adapter.items = it.toList()
                    }
            }

            val manager = recyclerView.layoutManager as GridLayoutManager
            val scrollListener =
                MainScreenScrollListener(
                    manager,
                    tabLayout,
                    contentListSize,
                    anchors,
                    requireContext()
                )

            tabLayout.addOnTabSelectedListener(scrollListener.TabLayoutTabListener())
            recyclerView.addOnScrollListener(scrollListener.RecyclerScrollListener())
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.main_menu, menu)

        val searchButton = menu.findItem(R.id.searchButton)
        val cartButton = menu.findItem(R.id.cartButton)

        searchButton.setOnMenuItemClickListener {
            navigateToSearchFragment()
            true
        }
    }

    private fun navigateToSearchFragment() {
        findNavController().navigate(R.id.action_mainScreenFragment_to_searchFragment)
    }

    private fun TabLayout.setNewAnchorTabs(anchors: List<String>) {
        removeAllTabs()
        if (anchors.contains("_")) {
            return
        }
        anchors.onEach {
            addTab(this.newTab().setText(it))
        }
        if (anchors.size > 3) tabMode = TabLayout.MODE_SCROLLABLE // todo edit
    }
}
