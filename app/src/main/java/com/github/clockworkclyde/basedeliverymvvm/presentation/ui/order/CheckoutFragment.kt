package com.github.clockworkclyde.basedeliverymvvm.presentation.ui.order

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.github.clockworkclyde.basedeliverymvvm.R
import com.github.clockworkclyde.basedeliverymvvm.databinding.FragmentCheckoutBinding
import com.github.clockworkclyde.basedeliverymvvm.presentation.ui.address.SearchAddressViewModel
import com.github.clockworkclyde.basedeliverymvvm.presentation.ui.auth.AuthViewModel
import com.github.clockworkclyde.basedeliverymvvm.presentation.ui.base.BaseFragment
import com.github.clockworkclyde.basedeliverymvvm.util.launchWhenResumed
import com.github.clockworkclyde.basedeliverymvvm.util.onSingleClick
import com.github.clockworkclyde.models.remote.base.Response
import com.github.clockworkclyde.models.ui.address.AddressItem
import com.google.android.material.transition.MaterialSharedAxis
import com.mapbox.api.staticmap.v1.MapboxStaticMap
import com.mapbox.api.staticmap.v1.StaticMapCriteria
import com.mapbox.api.staticmap.v1.models.StaticMarkerAnnotation
import com.mapbox.geojson.Point
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.*
import timber.log.Timber

@AndroidEntryPoint
class CheckoutFragment : BaseFragment(R.layout.fragment_checkout) {

    override var bottomNavigationViewVisibility: Int = View.GONE
    private lateinit var binding: FragmentCheckoutBinding

    private val glide by lazy { Glide.with(this) }
    private val orderViewModel: CheckoutViewModel by activityViewModels()
    private val authViewModel: AuthViewModel by activityViewModels()
    private val searchAddressViewModel: SearchAddressViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCheckoutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        applySearchView()
        applyOrderPhone()
        applyOrderPayment()
        observeAddressUpdates()
        observeUserData()
        observeBackStackEntry()
        binding.checkoutButton.onSingleClick { navigateToOrderProgress() }
    }

    private fun applySearchView() {
        with(binding) {
            mapViewLayout.setOnClickListener { navigateToEnterAddressOnMap() }
            addressLayout.setOnClickListener { navigateToSearchAddress() }
            totalPrice.text = "${orderViewModel.totalPrice.value} ₽."
        }
    }

    private fun applyOrderPhone() {
        setOnPhoneClickListener()
        orderViewModel.orderPhone.observe(viewLifecycleOwner) {
            Timber.e(it)
            setPhoneNumber(it) }
    }

    private fun applyOrderPayment() {
        setOnPaymentClickListener()
        orderViewModel.orderPaymentId.observe(viewLifecycleOwner) {
            binding.paymentMethod.text = getString(it)
        }
    }

    private fun observeAddressUpdates() {
        orderViewModel.orderAddress
                // todo emit getDefault() in orderAddress flow
            .onStart { emit(getDefaultAddress()) }
            .filterNotNull()
            .onEach { address ->
                try {
                    setOrderAddress(address.coordinates!!, address.street, address.house)
                } catch (e: Exception) {
                    Timber.e(e)
                    showToast("Got error with coordinates of ${address.addressId}")
                }
            }
            .launchWhenResumed(lifecycleScope)
    }

    private fun setOrderAddress(point: Point, street: String, house: String = "") {
        binding.apply {
            addressTextView.text = street
            if (house.isNotEmpty()) houseNumber.setText(house)
            glide.load(getMapImageUrl(point)).into(mapImageView)
        }
    }

    private fun getMapImageUrl(point: Point): String {
        val staticImage = MapboxStaticMap.builder()
            .accessToken(getString(R.string.mapbox_public_token))
            .styleId(StaticMapCriteria.STREET_STYLE)
            .cameraPoint(point)
            .cameraZoom(16.0)
            .staticMarkerAnnotations(listOf(getAnnotationAt(point)))
            .width(400)
            .height(200)
            .retina(true)
            .build()

        return staticImage.url().toString()
    }

    private fun getAnnotationAt(point: Point): StaticMarkerAnnotation {
        return StaticMarkerAnnotation.builder().lnglat(point).build()
    }

    private suspend fun getDefaultAddress(): AddressItem? {
        return searchAddressViewModel.userDefaultAddressOrNull()
    }

    private fun navigateToEnterAddressOnMap() {
        exitTransition = MaterialSharedAxis(MaterialSharedAxis.Z, true).apply {
            duration = resources.getInteger(R.integer.motion_duration_large).toLong()
        }
        reenterTransition = MaterialSharedAxis(MaterialSharedAxis.Z, false).apply {
            duration = resources.getInteger(R.integer.motion_duration_large).toLong()
        }
        findNavController().navigate(CheckoutFragmentDirections.actionToEnterAddressFragment())
    }

    private fun observeUserData() {
        authViewModel.getCurrentUser().observe(viewLifecycleOwner) { response ->
            when (response) {
                is Response.Error -> showToast("Error requesting user phone number with error: ${response.error}")
                is Response.Success -> {
                    val phone = response.data.phone
                    if (phone.isNotEmpty()) {
                        updatePhoneNumber(phone)
                    } else {
                        showToast("Phone number error! It's empty.")
                    }
                }
            }
        }
    }

    private fun observeBackStackEntry() {
        findNavController().currentBackStackEntryFlow
            .map { it.savedStateHandle.get<String>("phone") }
            .filterNotNull()
            .onEach { updatePhoneNumber(it) }
            .launchWhenResumed(lifecycleScope)
    }

    private fun setOnPhoneClickListener() {
        binding.phoneInfoLayout.onSingleClick { navigateToEditOrderPhoneNumber() }
    }

    private fun setOnPaymentClickListener() {
        binding.paymentMethodLayout.onSingleClick {
            navigateToEditOrderPayment()
        }
    }

    private fun updatePhoneNumber(phone: String) {
        orderViewModel.setOrderPhone(phone)
    }

    private fun setPhoneNumber(phone: String) {
        binding.phoneInfo.apply {
            text = phone
        }
    }

    private fun navigateToEditOrderPhoneNumber() {
        findNavController().navigate(CheckoutFragmentDirections.actionCheckoutFragmentToEditOrderPhoneFragment())
    }

    private fun navigateToEditOrderPayment() {
        findNavController().navigate(CheckoutFragmentDirections.actionCheckoutFragmentToEditOrderPaymentFragment())
    }

    private fun navigateToSearchAddress() {
        findNavController().navigate(CheckoutFragmentDirections.actionCheckoutFragmentToSearchAddressFragment())
    }

    private fun navigateToOrderProgress() {
        findNavController().navigate(CheckoutFragmentDirections.actionToOrderHistoryDetailsFragment())
    }

    private fun showToast(text: String) {
        Toast.makeText(
            requireContext(), text,
            Toast.LENGTH_SHORT
        ).show()
    }
}