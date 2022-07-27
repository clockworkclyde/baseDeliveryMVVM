package com.github.clockworkclyde.basedeliverymvvm.ui.screens

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupActionBarWithNavController
import com.github.clockworkclyde.basedeliverymvvm.R
import com.github.clockworkclyde.basedeliverymvvm.databinding.ActivityMainBinding
import com.github.clockworkclyde.basedeliverymvvm.ui.navigation.OnBottomSheetCallback
import com.google.android.material.bottomsheet.BottomSheetBehavior
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private var listener: OnBottomSheetCallback? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.elevation = 0f

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
        navController.addOnDestinationChangedListener { _, _, _ -> currentFocus?.hideKeyboard() }

        configureBackdrop()
        setupActionBarWithNavController(navController)
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp()
    }

    private fun View.hideKeyboard() {
        val imm = context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(windowToken, 0)
    }

    /** bottom sheet logic **/
    fun setOnBottomSheetListener(onBottomSheetCallback: OnBottomSheetCallback) {
        this.listener = onBottomSheetCallback
    }

    fun hideBottomSheet() {
        mBottomSheetBehaviour?.state = BottomSheetBehavior.STATE_HIDDEN
    }

    fun openBottomSheet() {
        mBottomSheetBehaviour?.state = BottomSheetBehavior.STATE_EXPANDED
    }

    private var mBottomSheetBehaviour: BottomSheetBehavior<View?>? = null

    private fun configureBackdrop() {
        val fragment = supportFragmentManager.findFragmentById(R.id.bottom_sheet_fragment)

        fragment?.view?.let {
            BottomSheetBehavior.from(it).let { bs ->
                bs.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
                    override fun onStateChanged(bottomSheet: View, newState: Int) {
                        listener?.onStateChanged(bottomSheet, newState)
                    }

                    override fun onSlide(bottomSheet: View, slideOffset: Float) {}
                })

                bs.state = BottomSheetBehavior.STATE_EXPANDED
                mBottomSheetBehaviour = bs
            }
        }
    }
}