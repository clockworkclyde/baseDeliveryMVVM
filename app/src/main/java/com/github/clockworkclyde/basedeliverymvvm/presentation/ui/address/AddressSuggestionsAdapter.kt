package com.github.clockworkclyde.basedeliverymvvm.presentation.ui.address

import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.github.clockworkclyde.basedeliverymvvm.R
import com.github.clockworkclyde.basedeliverymvvm.databinding.ItemSuggestionBinding
import com.github.clockworkclyde.basedeliverymvvm.util.BaseDiffUtilCallback
import com.github.clockworkclyde.models.ui.base.ListItem
import com.github.clockworkclyde.models.ui.address.AddressItem
import com.hannesdorfmann.adapterdelegates4.AsyncListDifferDelegationAdapter
import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegateViewBinding

data class SearchSuggestionItem(val id: String, val title: String) : ListItem {
    override val itemId: Long = id.hashCode().toLong()
}

class AddressSuggestionsAdapter(
    onUserAddressClick: (Int) -> Unit,
    onSearchResultClick: (Int) -> Unit
) :
    AsyncListDifferDelegationAdapter<ListItem>(BaseDiffUtilCallback()) {

    init {
        delegatesManager
            .addDelegate(userAddressesSuggestionDelegate(onUserAddressClick = onUserAddressClick))
            .addDelegate(searchResultsSuggestionDelegate(onSearchResultClick = onSearchResultClick))
    }

    companion object {
        fun userAddressesSuggestionDelegate(onUserAddressClick: (Int) -> Unit) =
            adapterDelegateViewBinding<AddressItem, ListItem, ItemSuggestionBinding>({ inflater, container ->
                ItemSuggestionBinding.inflate(inflater, container, false)
            }) {
                bind {
                    with(binding) {
                        typeImageView.setImageDrawable(root.context.getDrawable(R.drawable.ic_baseline_access_time_24))
                        titleTextView.text = item.name
                        checkImageView.isVisible = item.isDefault
                        root.setOnClickListener {
                            if (bindingAdapterPosition != RecyclerView.NO_POSITION) {
                                onUserAddressClick.invoke(bindingAdapterPosition)
                            }
                        }
                    }
                }
            }

        fun searchResultsSuggestionDelegate(onSearchResultClick: (Int) -> Unit) =
            adapterDelegateViewBinding<SearchSuggestionItem, ListItem, ItemSuggestionBinding>({ inflater, container ->
                ItemSuggestionBinding.inflate(inflater, container, false)
            }) {
                bind {
                    with(binding) {
                        typeImageView.setImageDrawable(root.context.getDrawable(R.drawable.ic_baseline_location_on_24))
                        titleTextView.text = item.title
                        root.setOnClickListener {
                            if (bindingAdapterPosition != RecyclerView.NO_POSITION) {
                                onSearchResultClick.invoke(bindingAdapterPosition)
                            }
                        }
                    }
                }
            }
    }
}