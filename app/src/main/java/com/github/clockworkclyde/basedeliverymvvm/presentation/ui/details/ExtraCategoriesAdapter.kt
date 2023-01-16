package com.github.clockworkclyde.basedeliverymvvm.presentation.ui.details

import com.github.clockworkclyde.basedeliverymvvm.databinding.ItemExtrasCategoryBinding
import com.github.clockworkclyde.basedeliverymvvm.util.BaseDiffUtilCallback
import com.github.clockworkclyde.models.ui.base.ListItem
import com.github.clockworkclyde.models.ui.dishes.extra.ExtraCategory
import com.hannesdorfmann.adapterdelegates4.AsyncListDifferDelegationAdapter
import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegateViewBinding

class ExtraCategoriesAdapter(onExtraClickListener: OnExtraClickListener) :
   AsyncListDifferDelegationAdapter<ListItem>(BaseDiffUtilCallback()) {

   init {
      delegatesManager.addDelegate(extraCategoryDelegate(onExtraClickListener = onExtraClickListener))
   }

   companion object {
      private fun extraCategoryDelegate(onExtraClickListener: OnExtraClickListener) =
         adapterDelegateViewBinding<ExtraCategory, ListItem, ItemExtrasCategoryBinding>({ inflater, container ->
            ItemExtrasCategoryBinding.inflate(inflater, container, false)
         }) {
            val adapter = MultipleExtrasAdapter(
               onExtraClickListener = onExtraClickListener,
               isForSingleSelect = false,
               isQuantityAccepted = false
            ).also {
               binding.extrasRecyclerView.adapter = it
            }
            bind {
               binding.titleView.text = item.title
               adapter.items = item.extras
            }
         }
   }
}