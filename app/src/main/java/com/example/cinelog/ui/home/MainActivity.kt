package com.example.cinelog.ui.home

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.example.cinelog.R
import com.example.cinelog.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private var notificationCount: Int = 0

    private val notificationCountReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val count = intent?.getIntExtra("notification_count", 0) ?: 0
            runOnUiThread {
                updateBadgeCount(count) // Ensure UI is updated on the main thread
            }
        }

    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.movieListFragment,
                R.id.graphFragment,
                R.id.favoritesFragment,
                R.id.othersFragment
            )
        )

        NavigationUI.setupWithNavController(binding.bottomNav, navController)

        val filter = IntentFilter("com.example.cinelog.UPDATE_NOTIFICATION_COUNT")
        registerReceiver(notificationCountReceiver, filter,Context.RECEIVER_NOT_EXPORTED)

        val sharedPreferences = getSharedPreferences("notification_prefs", MODE_PRIVATE)
        notificationCount = sharedPreferences.getInt("notification_count", 0)
        updateBadgeCount(notificationCount)

    }

    private fun updateBadgeCount(count: Int) {
        val bottomNav = binding.bottomNav
        val badge = bottomNav.getOrCreateBadge(R.id.othersFragment) // Ensure correct menu item ID
        if (count > 0) {
            badge.isVisible = true
            badge.number = count
        } else {
            badge.isVisible = false
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onResume() {
        super.onResume()
        val filter = IntentFilter("com.example.cinelog.UPDATE_NOTIFICATION_COUNT")
        registerReceiver(notificationCountReceiver, filter, Context.RECEIVER_NOT_EXPORTED)
    }

    override fun onPause() {
        super.onPause()
        unregisterReceiver(notificationCountReceiver) // Unregister to avoid leaks
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    override fun onDestroy() {
        super.onDestroy()
        // Unregister the receiver when the activity is destroyed
        unregisterReceiver(notificationCountReceiver)
    }
}