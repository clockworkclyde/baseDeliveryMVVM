package com.github.clockworkclyde.basedeliverymvvm.data.repository

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asFlow
import com.github.clockworkclyde.models.ui.address.AddressItem
import com.mapbox.search.*
import com.mapbox.search.result.SearchResult
import com.mapbox.search.result.SearchSuggestion
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class SearchAddressRepository @Inject constructor(private val searchEngine: SearchEngine) {

    private val resultsFlow = MutableStateFlow<List<SearchSuggestion>>(listOf())

    private val searchOptions by lazy {
        SearchOptions.Builder()
            //.proximity(Point.fromLngLat(61.261710, 73.377467)) // todo delete
            .limit(30)
            .countries(Country.RUSSIA)
            .build()
    }

    private val searchSuggestionsCallback = object : SearchSuggestionsCallback {
        override fun onError(e: Exception) {
            // TODO: Error handle
        }

        override fun onSuggestions(
            suggestions: List<SearchSuggestion>,
            responseInfo: ResponseInfo
        ) {
            resultsFlow.value = if (suggestions.isNotEmpty()) {
                suggestions.filter { it.address?.formattedAddress() != null }
            } else {
                listOf()
            }
        }
    }

    private val _suggestionResult = MutableLiveData<AddressItem>()

    private val selectionCallback = object : SearchSelectionCallback {
        override fun onCategoryResult(
            suggestion: SearchSuggestion,
            results: List<SearchResult>,
            responseInfo: ResponseInfo
        ) {
        }

        override fun onError(e: Exception) {
            // TODO: Error handle
        }

        override fun onSuggestions(
            suggestions: List<SearchSuggestion>,
            responseInfo: ResponseInfo
        ) {
        }

        override fun onResult(
            suggestion: SearchSuggestion,
            result: SearchResult,
            responseInfo: ResponseInfo
        ) {
            val item = AddressItem(
                id = result.id,
                name = result.name,
                coordinates = result.coordinate,
                street = result.address?.street ?: "",
                house = result.address?.houseNumber ?: ""
            )
            _suggestionResult.value = item
        }
    }

    fun search(query: String) = flow {
        searchEngine.search(query, searchOptions, searchSuggestionsCallback)
        emitAll(resultsFlow.filter { it.isNotEmpty() })
    }

    fun selectSuggestion(suggestion: SearchSuggestion) = flow {
        searchEngine.select(suggestion, selectionCallback)
        emitAll(_suggestionResult.asFlow())
    }

}