package com.github.clockworkclyde.basedeliverymvvm.domain.address

import com.github.clockworkclyde.basedeliverymvvm.data.repository.SearchAddressRepository
import com.mapbox.search.result.SearchSuggestion
import javax.inject.Inject

class SelectSearchSuggestionUseCase @Inject constructor(private val repository: SearchAddressRepository) {

    operator fun invoke(suggestion: SearchSuggestion) = repository.selectSuggestion(suggestion)
}