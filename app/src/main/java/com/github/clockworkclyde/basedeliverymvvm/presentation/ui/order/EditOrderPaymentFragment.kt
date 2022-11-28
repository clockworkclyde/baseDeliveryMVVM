package com.github.clockworkclyde.basedeliverymvvm.presentation.ui.order

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.fragment.app.activityViewModels
import com.github.clockworkclyde.basedeliverymvvm.R
import com.github.clockworkclyde.basedeliverymvvm.databinding.FragmentEditPaymentMethodBinding
import com.github.clockworkclyde.basedeliverymvvm.presentation.ui.base.BaseDialogFragment

// TODO: move to models
sealed class PaymentMethod(val id: Int) {
    object CreditCard : PaymentMethod(R.string.payment_method_by_card)
    object Cash : PaymentMethod(R.string.payment_method_by_cash)
}

class EditOrderPaymentFragment : BaseDialogFragment() {

    private lateinit var binding: FragmentEditPaymentMethodBinding
    private val viewModel: CheckoutViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEditPaymentMethodBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        applyButtons()
        setOnCheckedListener()
    }

    private fun applyButtons() {
        with(binding) {
            getMethods().forEach { item ->
                RadioButton(requireContext()).apply {
                    id = item.id
                    text = getString(item.id)
                    textSize = 17.0f
                    isChecked = item.id == viewModel.orderPaymentId.value
                    // TODO: remove it, add color/size to app theme RadioButton
                    buttonTintList = ColorStateList.valueOf(
                        resources.getColor(
                            R.color.dishes_add_to_cart_button,
                            requireContext().theme
                        )
                    )
                }.also {
                    radioGroup.addView(it)
                }
            }
        }
    }

    private fun getMethods(): List<PaymentMethod> {
        return listOf(PaymentMethod.CreditCard, PaymentMethod.Cash)
    }

    private fun setOnCheckedListener() {
        binding.radioGroup.setOnCheckedChangeListener { group, checkedId ->
            val button = group.findViewById<RadioButton>(checkedId)
            viewModel.setOrderPayment(button.id)
        }
    }

}