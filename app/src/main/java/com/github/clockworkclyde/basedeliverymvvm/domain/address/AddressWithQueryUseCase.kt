package com.github.clockworkclyde.basedeliverymvvm.domain.address

import com.github.clockworkclyde.basedeliverymvvm.data.repository.SearchAddressRepository
import javax.inject.Inject

class AddressWithQueryUseCase @Inject constructor(private val repository: SearchAddressRepository) {

    operator fun invoke(query: String) =
        repository.search(query) //.map { suggestions -> suggestions.map { it.convertTo() } }

//    private fun SearchSuggestion.convertTo() =
//        SearchSuggestionItem(
//            id = id,
//            title = address!!.formattedAddress().toString(),
//        )
}