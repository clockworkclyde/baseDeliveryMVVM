package com.github.clockworkclyde.basedeliverymvvm.presentation.ui.address

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import com.github.clockworkclyde.basedeliverymvvm.domain.address.AddUserAddressesUseCase
import com.github.clockworkclyde.basedeliverymvvm.presentation.ui.base.BaseViewModel
import com.github.clockworkclyde.models.remote.address.AddressModel
import com.github.clockworkclyde.models.ui.base.ViewState
import com.github.clockworkclyde.models.ui.address.AddressItem
import com.mapbox.geojson.Point
import com.mapbox.search.ResponseInfo
import com.mapbox.search.ReverseGeoOptions
import com.mapbox.search.SearchCallback
import com.mapbox.search.SearchEngine
import com.mapbox.search.result.SearchResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val REQ_THROTTLING_DELAY = 2000L

@HiltViewModel
class AddressViewModel @Inject constructor(
    private val searchEngine: SearchEngine,
    private val addAddress: AddUserAddressesUseCase
) : BaseViewModel() {

    private val _viewState = MutableLiveData<ViewState<AddressItem>>()
    val viewState: LiveData<ViewState<AddressItem>> get() = _viewState

    private val _errorRequest = MutableLiveData<Throwable>()
    val errorRequest: LiveData<Throwable> get() = _errorRequest

    /** Сохранить адрес пользователя **/
    fun postUserAddress(address: AddressItem) = viewModelScope.launch { addAddress(address) }

    private val _currentLocation = MutableStateFlow<Point?>(null)

    fun setCurrentLocation(point: Point) {
        _currentLocation.tryEmit(point)
    }

    fun resetCurrentLocation() {
        _currentLocation.tryEmit(null)
    }

    suspend fun launchAddressSearch() {
        searchAddressWithCurrentLocation().collect {
            _viewState.postValue(it)
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
    suspend fun searchAddressWithCurrentLocation() = _currentLocation
        .debounce(REQ_THROTTLING_DELAY)
        .filterNotNull()
        .flatMapLatest { point ->
            flow {
                emit(ViewState.Loading)
                search(point).filterNotNull().collect {
                    emit(ViewState.Success(it))
                }
            }
        }

    private fun search(point: Point) = flow {
        val options = ReverseGeoOptions(center = point, limit = 1)
        searchEngine.search(options, searchCallback)
        emitAll(resultAddress.asFlow())
    }

    private val resultAddress = MutableLiveData<AddressItem?>()

    private val searchCallback = object : SearchCallback {

        override fun onError(e: Exception) {
            _errorRequest.postValue(e)
        }

        override fun onResults(results: List<SearchResult>, responseInfo: ResponseInfo) {
            if (results.isEmpty()) {
                resultAddress.value = null
            } else {
                val minDistanceResult = results.sortedWith(compareBy { it.distanceMeters }).first()
                val address = mapToAddressModel(minDistanceResult)
                resultAddress.value = address.convertTo()
            }

        }
    }

    /** Search result mapper **/
    private fun mapToAddressModel(result: SearchResult) =
        AddressModel(
            id = result.id,
            name = result.name,
            address = result.address,
            coordinates = result.coordinate
        )
}
