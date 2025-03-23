package com.example.cinelog.util

import android.content.Context
import androidx.appcompat.app.AppCompatDelegate

class SaveThemeSettings(private val context: Context) {

    companion object {
        private const val PREFS_NAME = "AppPreferences"
        private const val KEY_DARK_MODE = "DarkMode"
    }

    private val sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun saveThemePreference(isDarkMode: Boolean) {
        sharedPreferences.edit().putBoolean(KEY_DARK_MODE, isDarkMode).apply()
        applyTheme(isDarkMode)
    }

    fun loadThemePreference() {
        val isDarkMode = sharedPreferences.getBoolean(KEY_DARK_MODE, false)
        applyTheme(isDarkMode)
    }

    fun isDarkModeEnabled(): Boolean {
        return sharedPreferences.getBoolean(KEY_DARK_MODE, false)
    }

    private fun applyTheme(isDarkMode: Boolean) {
        val currentMode = AppCompatDelegate.getDefaultNightMode()
        val newMode = if (isDarkMode) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO
        if (currentMode != newMode) {
            AppCompatDelegate.setDefaultNightMode(newMode)
        }
    }
}