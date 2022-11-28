package com.github.clockworkclyde.basedeliverymvvm.presentation.ui.address

import androidx.lifecycle.*
import com.chibatching.kotpref.livedata.asLiveData
import com.github.clockworkclyde.basedeliverymvvm.domain.address.AddressWithQueryUseCase
import com.github.clockworkclyde.basedeliverymvvm.domain.address.GetSpecificUserAddressUseCase
import com.github.clockworkclyde.basedeliverymvvm.domain.address.GetUserAddressesUseCase
import com.github.clockworkclyde.basedeliverymvvm.domain.address.SelectSearchSuggestionUseCase
import com.github.clockworkclyde.basedeliverymvvm.presentation.ui.base.BaseViewModel
import com.github.clockworkclyde.models.local.cart.OrderDetailsPref
import com.github.clockworkclyde.models.remote.base.Response
import com.github.clockworkclyde.models.ui.base.ListItem
import com.github.clockworkclyde.models.ui.base.ViewState
import com.github.clockworkclyde.models.ui.address.AddressItem
import com.mapbox.search.result.SearchSuggestion
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchAddressViewModel @Inject constructor(
    private val getAddressById: GetSpecificUserAddressUseCase,
    private val getAddresses: GetUserAddressesUseCase,
    private val getAddressByQuery: AddressWithQueryUseCase,
    private val selectSuggestion: SelectSearchSuggestionUseCase
) : BaseViewModel() {

    private val _viewState = MutableLiveData<ViewState<List<ListItem>>>()
    val viewState: LiveData<ViewState<List<ListItem>>> = _viewState

    private val _inputText = MutableLiveData<String>()
    val inputText: LiveData<String> = _inputText

    private val _selectedAddress = MutableStateFlow<AddressItem?>(null)
    val selectedAddress = _selectedAddress.asStateFlow()

    private val _searchQuery = MutableStateFlow("")

    fun updateQuery(query: String) {
        _searchQuery.tryEmit(query)
    }

    private fun postInputText(newText: String) {
        _inputText.postValue(newText)
    }

    private val _suggestions = MutableStateFlow<List<SearchSuggestion>>(listOf())

    suspend fun collectSearchEvent() {
        _searchQuery.flatMapConcat {
            if (it.isNotEmpty()) {
                searchResult()
            } else {
                val result = tryToLoadUserAddresses()
                    .filterIsInstance<Response.Success<List<AddressItem>>>()
                    .map { response -> ViewState.Success(response.data) }
                result
            }
        }
            .collect {
                _viewState.postValue(it)
            }
    }

    @OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
    private fun searchResult() = _searchQuery
        .debounce(300L)
        .filter { it.trim().isNotEmpty() }
        .distinctUntilChanged()
        .flatMapLatest {
            flow {
                emit(ViewState.Empty)
                emit(ViewState.Loading)
                getAddressByQuery(it).collect { suggestions ->
                    _suggestions.emit(suggestions)
                    emit(ViewState.Success(suggestions.map { it.convertTo() }))
                }
            }
        }


    /** Адреса пользователя, хранящиеся на бекэнде **/
    private fun tryToLoadUserAddresses() = flow {
        emit(getAddresses())
    }

    fun onUserAddressClick(index: Int) {
        _viewState.value?.let {
            if (it is ViewState.Success) {
                val item = it.data[index]
                if (item is AddressItem) {
                    _selectedAddress.tryEmit(item)
                    OrderDetailsPref.addressId = item.addressId
                }
            }
        }
    }

    suspend fun userDefaultAddressOrNull(): AddressItem? {
        return getAddressById(OrderDetailsPref.addressId)
    }

    private fun userDefaultAddressLiveData() =
        OrderDetailsPref.asLiveData(OrderDetailsPref::addressId).switchMap { id ->
            liveData {
                emit(getAddressById(id))
            }
        }

    fun getSearchSuggestionData(index: Int) = viewModelScope.launch {
        _suggestions.collect { suggestions ->
            selectSuggestion(suggestions[index]).collect {
                _selectedAddress.tryEmit(it)
                postInputText(it.street + " " + it.house)
            }
        }
    }

    companion object {
        private fun SearchSuggestion.convertTo() =
            SearchSuggestionItem(
                id = id,
                title = address!!.formattedAddress().toString(),
            )
    }
}