package com.example.cinelog.ui.home

import android.content.Intent
import android.os.Bundle

import androidx.appcompat.app.AppCompatActivity

import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.example.cinelog.R
import com.example.cinelog.databinding.ActivityMainBinding
import com.example.cinelog.ui.history.HistoryActivity
import com.example.cinelog.util.SaveThemeSettings

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
        val themeSettings = SaveThemeSettings(this)
        themeSettings.loadThemePreference()
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.movieListFragment,
                R.id.graphFragment,
                R.id.movieListFragment,
                R.id.favoritesFragment,
                R.id.othersFragment
            )
        )
        NavigationUI.setupWithNavController(binding.bottomNav, navController)
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}