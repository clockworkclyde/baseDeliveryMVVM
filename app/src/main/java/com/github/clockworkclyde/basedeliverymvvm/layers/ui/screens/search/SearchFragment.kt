package com.github.clockworkclyde.basedeliverymvvm.layers.ui.screens.search

import android.os.Bundle
import android.view.*
import android.widget.EditText
import android.widget.SearchView
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.github.clockworkclyde.basedeliverymvvm.R
import com.github.clockworkclyde.basedeliverymvvm.databinding.FragmentSearchBinding
import com.github.clockworkclyde.basedeliverymvvm.layers.ui.navigation.OnBottomSheetCallback
import com.github.clockworkclyde.basedeliverymvvm.layers.ui.screens.MainActivity
import com.github.clockworkclyde.basedeliverymvvm.layers.ui.screens.main.MainScreenAdapter
import com.github.clockworkclyde.basedeliverymvvm.layers.ui.util.doOnQueryTextChanged
import com.github.clockworkclyde.basedeliverymvvm.layers.ui.vm.search.SearchViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class SearchFragment : Fragment(R.layout.fragment_search) {

    private lateinit var binding: FragmentSearchBinding
    private val viewModel: SearchViewModel by viewModels()

    private val adapter by lazy { MainScreenAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            recyclerView.adapter = adapter

            viewLifecycleOwner.lifecycleScope.launchWhenCreated {
                viewModel.data.collect {
                    adapter.items = it
                    if (it.isEmpty()) showEmptyResult()
                }
            }
            setHasOptionsMenu(true)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.search_menu, menu)

        val searchItem = menu.findItem(R.id.action_fragment_search)
        val searchView = searchItem.actionView as SearchView
        searchView.apply {
            doOnQueryTextChanged { viewModel.search(it) }
            isIconified = false
        }
        searchItem.expandActionView()
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)

    }

    private fun showEmptyResult() {
        Toast.makeText(requireContext(), "Empty result", Toast.LENGTH_SHORT).show()
    }
}


// Открывается экран, на экране панель поиска и кнопка назад, сразу под ними небольшой список последних запросов,
// для того чтобы предоставить пользователю альтернативый способ при заходе в приложение находить то что он уже покупал
//
// Вместе с изменением запроса в поисковой строке,
