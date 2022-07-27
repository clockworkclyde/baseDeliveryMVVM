package com.github.clockworkclyde.basedeliverymvvm.ui.screens.search

import android.os.Bundle
import android.view.*
import android.widget.SearchView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.github.clockworkclyde.basedeliverymvvm.R
import com.github.clockworkclyde.basedeliverymvvm.databinding.FragmentSearchBinding
import com.github.clockworkclyde.basedeliverymvvm.ui.screens.main.MainScreenAdapter
import com.github.clockworkclyde.basedeliverymvvm.ui.util.doOnQueryTextChanged
import com.github.clockworkclyde.basedeliverymvvm.ui.vm.search.SearchViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchFragment : Fragment(R.layout.fragment_search) {

    private lateinit var binding: FragmentSearchBinding
    private val viewModel: SearchViewModel by viewModels()

    private val adapter by lazy { MainScreenAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        with(binding) {
            recyclerView.adapter = adapter
            recyclerView.setHasFixedSize(true)

            viewLifecycleOwner.lifecycleScope.launchWhenCreated {
                viewModel.data.collect {
                    adapter.items = it
                    if (it.isEmpty()) showEmptyResult()
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.search_menu, menu)

        (menu.findItem(R.id.action_fragment_search).actionView as SearchView).apply {
            doOnQueryTextChanged {
                viewModel.search(it)
                binding.recyclerView.smoothScrollToPosition(0)
            }
            isIconifiedByDefault = true
            queryHint = "For example, Grill"
            onActionViewExpanded()
        }
    }

    private fun showEmptyResult() {
        Toast.makeText(requireContext(), "Empty result", Toast.LENGTH_SHORT).show()
    }
}
