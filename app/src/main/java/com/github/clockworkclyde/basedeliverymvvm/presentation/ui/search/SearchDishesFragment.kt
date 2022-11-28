package com.github.clockworkclyde.basedeliverymvvm.presentation.ui.search

import android.graphics.Color
import android.os.Bundle
import android.view.*
import android.widget.SearchView
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.transition.Slide
import com.github.clockworkclyde.basedeliverymvvm.R
import com.github.clockworkclyde.basedeliverymvvm.databinding.FragmentSearchDishesBinding
import com.github.clockworkclyde.basedeliverymvvm.presentation.ui.base.BaseFragment
import com.github.clockworkclyde.basedeliverymvvm.presentation.ui.dishes.OnDishItemClickListener
import com.github.clockworkclyde.basedeliverymvvm.presentation.ui.order.OrderCartViewModel
import com.github.clockworkclyde.basedeliverymvvm.util.doOnQueryTextChanged
import com.github.clockworkclyde.basedeliverymvvm.util.launchWhenResumed
import com.github.clockworkclyde.models.ui.dishes.DishItem
import com.github.clockworkclyde.models.ui.order.OrderDish
import com.google.android.material.transition.MaterialContainerTransform
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.*

@AndroidEntryPoint
class SearchDishesFragment : BaseFragment(R.layout.fragment_search_dishes),
    OnDishItemClickListener {

    override var bottomNavigationViewVisibility: Int = View.GONE

    private lateinit var binding: FragmentSearchDishesBinding
    private val viewModel: SearchDishesViewModel by viewModels()
    private val orderViewModel: OrderCartViewModel by viewModels()
    private val adapter by lazy { SearchDishesAdapter(this) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchDishesBinding.inflate(inflater, container, false)
        applyMaterialContainerTransform()
        return binding.root
    }

    private fun applyMaterialContainerTransform() {
        enterTransition = MaterialContainerTransform().apply {
            startView = requireActivity().findViewById(R.id.searchButton)
            endView = binding.root
            duration = resources.getInteger(R.integer.motion_duration_large).toLong()
            scrimColor = Color.TRANSPARENT
        }
        returnTransition = Slide().apply {
            duration = resources.getInteger(R.integer.motion_duration_large).toLong()
            addTarget(R.id.dishesRootLayout)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        with(binding) {
            recyclerView.adapter = adapter
            recyclerView.setHasFixedSize(true)
            viewModel.data.onEach {
                adapter.items = it
                if (it.isEmpty()) showEmptyResult()
            }.launchWhenResumed(lifecycleScope)
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
            queryHint = context.getString(R.string.search_hint)
            onActionViewExpanded()
        }
    }

    private fun showEmptyResult() {
        Toast.makeText(requireContext(), getString(R.string.empty_result), Toast.LENGTH_SHORT)
            .show()
    }

    override fun onItemClick(item: DishItem) {
        findNavController().navigate(
            SearchDishesFragmentDirections.actionToDetailsFragment(
                item,
                emptyArray()
            )
        )
    }

    override fun onButtonClick(item: DishItem) {
        orderViewModel.addToOrderCart(item.id)
    }

    override fun onEditOrderDish(item: OrderDish) {
        findNavController().navigate(
            SearchDishesFragmentDirections.actionToDetailsFragment(
                item.dish,
                item.extras.toTypedArray()
            )
        )
    }
}
