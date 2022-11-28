package com.github.clockworkclyde.basedeliverymvvm.presentation.ui

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import com.github.clockworkclyde.basedeliverymvvm.R
import com.github.clockworkclyde.basedeliverymvvm.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private lateinit var navHostFragment: NavHostFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupApplicationActionBar()
        setupAllWithNavController()

        navController.addOnDestinationChangedListener { _, _, _ -> currentFocus?.hideKeyboard() }
    }

    private fun setupApplicationActionBar() {
        setSupportActionBar(binding.toolbar)
    }

    private fun setupAllWithNavController() {
        navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
        val appBarConfiguration =
            AppBarConfiguration(
                setOf(
                    R.id.dishesFragment,
                    R.id.orderCartFragment,
                    R.id.profileFragment
                    )
            )
        setupActionBarWithNavController(navController, appBarConfiguration)
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp()
    }

    fun getCurrentNavigationFragment(): Fragment {
        return navHostFragment.childFragmentManager.fragments.first()
    }

    fun getNavHostFragmentNavController(): NavController {
        return navController
    }

    fun setBottomNavigationViewVisibility(visibility: Int) {
        binding.bottomNavigationFragmentContainer.visibility = visibility
    }

    private fun View.hideKeyboard() {
        val imm = context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(windowToken, 0)
    }
}