package com.github.clockworkclyde.basedeliverymvvm.presentation.ui.address

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.github.clockworkclyde.basedeliverymvvm.R
import com.github.clockworkclyde.basedeliverymvvm.databinding.FragmentEnterAddressLocationBinding
import com.github.clockworkclyde.basedeliverymvvm.presentation.ui.base.BaseFragment
import com.github.clockworkclyde.basedeliverymvvm.presentation.ui.order.CheckoutViewModel
import com.github.clockworkclyde.basedeliverymvvm.util.launchWhenResumed
import com.github.clockworkclyde.basedeliverymvvm.util.onSingleClick
import com.github.clockworkclyde.basedeliverymvvm.util.setTextById
import com.github.clockworkclyde.basedeliverymvvm.util.toLatLng
import com.github.clockworkclyde.models.ui.base.ViewState
import com.github.clockworkclyde.models.ui.address.AddressItem
import com.google.android.material.transition.MaterialSharedAxis
import com.mapbox.android.core.permissions.PermissionsListener
import com.mapbox.android.core.permissions.PermissionsManager
import com.mapbox.android.gestures.MoveGestureDetector
import com.mapbox.geojson.Point
import com.mapbox.mapboxsdk.camera.CameraPosition
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory
import com.mapbox.mapboxsdk.location.LocationComponent
import com.mapbox.mapboxsdk.location.LocationComponentActivationOptions
import com.mapbox.mapboxsdk.location.modes.CameraMode
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback
import com.mapbox.mapboxsdk.maps.Style
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@AndroidEntryPoint
class EnterAddressLocationFragment : BaseFragment(R.layout.fragment_enter_address_location),
    OnMapReadyCallback,
    PermissionsListener, MapboxMap.OnMoveListener {

    override var bottomNavigationViewVisibility: Int = View.GONE

    private lateinit var binding: FragmentEnterAddressLocationBinding
    private lateinit var locationComponent: LocationComponent
    private lateinit var permissionsManager: PermissionsManager

    private var mapboxMap: MapboxMap? = null
    private val mapView by lazy { binding.mapView }

    private val viewModel: AddressViewModel by viewModels()
    private val orderViewModel: CheckoutViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEnterAddressLocationBinding.inflate(inflater, container, false)
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)
        viewModel.errorRequest.observe(viewLifecycleOwner) { showToast("Error: $it") }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        enterTransition = MaterialSharedAxis(MaterialSharedAxis.Z, true).apply {
            duration = resources.getInteger(R.integer.motion_duration_large).toLong()
        }
        returnTransition = MaterialSharedAxis(MaterialSharedAxis.Z, false).apply {
            duration = resources.getInteger(R.integer.motion_duration_large).toLong()
        }

        observeSearchResults()

    }

    override fun onResume() {
        super.onResume()
        lifecycleScope.launch { viewModel.launchAddressSearch() }
    }

    private fun observeSearchResults() {
        viewModel.viewState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is ViewState.Empty -> {
                    updateButtonState(R.string.address_empty)
                }
                is ViewState.Error -> {
                    showToast("Error: ${state.throwable}")
                }
                is ViewState.Loading -> {
                    updateButtonState(R.string.address_loading)
                }
                is ViewState.Success -> {
                    state.data.coordinates!!.let {
                        setAddress(state.data.street, state.data.house)
                        moveCameraToSpecificPoint(it)
                        updateButtonState(R.string.set_order_address_from_map, true)
                        binding.button.onSingleClick { setOrderAddress(state.data) }
                    }
                }
            }
        }
    }

    private fun updateButtonState(id: Int, isButtonEnabled: Boolean = false) {
        binding.button.apply {
            isEnabled = (isAddressValid() || isButtonEnabled)
            setTextById(id)
        }
    }

    private fun isAddressValid() =
        binding.street.text.isNotEmpty() && binding.houseNumber.text.isNotEmpty()

    private fun setAddress(street: String, houseNumber: String) {
        binding.apply {
            this.street.setText(street)
            this.houseNumber.setText(houseNumber)
        }
    }

    // todo move to baseFragment class
    private fun showToast(text: String) = Toast.makeText(requireContext(), text, Toast.LENGTH_SHORT)

    override fun onMapReady(mapboxMap: MapboxMap) {
        this.mapboxMap = mapboxMap
        mapboxMap.setStyle(Style.MAPBOX_STREETS) { style ->
            initLocationComponent(style)
            initCurrentLocation()
            getCurrentOrderAddressOnMap()
            mapboxMap.addOnMoveListener(this)
        }
    }

    private fun initCurrentLocation() {
        val address = orderViewModel.orderAddress.value
        if (address != null) {
            return
        } else {
            viewModel.setCurrentLocation(getCameraPositionLatLng())
        }
    }

    private fun getCurrentOrderAddressOnMap() {
        orderViewModel.orderAddress.onEach { address ->
            address?.let {
                setAddress(it.street, it.house)
                setNewCameraPosition(it.coordinates!!)
            }
        }.launchWhenResumed(lifecycleScope)
    }


    private fun setNewCameraPosition(point: Point) {
        mapboxMap?.cameraPosition = CameraPosition.Builder()
            .target(point.toLatLng())
            .build()
    }

    private fun moveCameraToSpecificPoint(point: Point) {
        mapboxMap?.animateCamera(
            CameraUpdateFactory.newCameraPosition(
                CameraPosition.Builder()
                    .target(point.toLatLng())
                    .build()
            ), resources.getInteger(R.integer.camera_position_move_duration)
        )
    }

    private fun setOrderAddress(address: AddressItem) {
        if (isAddressValid()) {
            with(binding) {
                orderViewModel.setOrderAddress(
                    address.copy(
                        street = street.text.toString(),
                        house = houseNumber.text.toString(),
                        apartment = apartmentNumber.text.toString(),
                        entrance = entrance.text.toString(),
                        doorPhone = doorPhone.text.toString()
                    )
                )
                findNavController().navigateUp()
            }
        } else {
            showToast("Вы не заполнили необходимые поля")
        }
    }

    // Camera move on map callbacks
    override fun onMoveBegin(detector: MoveGestureDetector) {}
    override fun onMove(detector: MoveGestureDetector) {
        viewModel.resetCurrentLocation()
    }

    override fun onMoveEnd(detector: MoveGestureDetector) {
        viewModel.setCurrentLocation(getCameraPositionLatLng())
    }

    private fun getCameraPositionLatLng(): Point {
        val target = mapboxMap!!.cameraPosition.target
        return Point.fromLngLat(target.longitude, target.latitude)
    }

    private fun initLocationComponent(style: Style) {
        if (PermissionsManager.areLocationPermissionsGranted(requireContext())) {
            locationComponent = mapboxMap!!.locationComponent
            val options =
                LocationComponentActivationOptions.Builder(requireContext(), style).build()
            locationComponent.activateLocationComponent(options)
            locationComponent.isLocationComponentEnabled = true
            locationComponent.cameraMode = CameraMode.TRACKING
        } else {
            permissionsManager = PermissionsManager(this)
            permissionsManager.requestLocationPermissions(requireActivity())
        }
    }

    override fun onExplanationNeeded(permissionsToExplain: MutableList<String>?) {}

    override fun onPermissionResult(granted: Boolean) {
        if (granted) {
            initLocationComponent(mapboxMap!!.style!!)
        } else {
            Toast.makeText(requireContext(), "Enable geo location", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onStart() {
        super.onStart()
        mapView.onStart()
    }

    override fun onStop() {
        super.onStop()
        mapView.onStop()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
    }

}