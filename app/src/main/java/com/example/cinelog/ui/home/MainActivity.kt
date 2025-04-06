package com.example.cinelog.ui.home

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.example.cinelog.R
import com.example.cinelog.data.local.sharedPref.SharedPrefHelper
import com.example.cinelog.databinding.ActivityMainBinding
import com.example.cinelog.util.SaveThemeSettings

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private lateinit var sharedPrefHelper: SharedPrefHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sharedPrefHelper = SharedPrefHelper(this)
        val themeSettings = SaveThemeSettings(this)
        themeSettings.loadThemePreference()
        setupNavigation(savedInstanceState)
    }
    private fun setupNavigation(savedInstanceState:Bundle?) {
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment

        val inflater = navHostFragment.navController.navInflater
        val graph = inflater.inflate(R.navigation.nav_graph)

        val startDest = if (sharedPrefHelper.isOnboardingCompleted) {
            R.id.main_nav_graph
        } else {
            R.id.onboardingFragment
        }

        graph.setStartDestination(startDest)

        navController = navHostFragment.navController

        navController.setGraph(graph,savedInstanceState)
        if (sharedPrefHelper.isOnboardingCompleted) {
            setupBottomNavigation()
        }
    }

    fun setupBottomNavigation() {
        NavigationUI.setupWithNavController(binding.bottomNav, navController)
        binding.bottomNav.visibility = View.VISIBLE
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}