package com.example.cinelog.ui.home.others

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Configuration
import android.os.Build
import java.util.Locale

object LanguageHelper {

    fun setLocale(context: Context, languageCode: String): Context {
        val locale = Locale(languageCode)
        Locale.setDefault(locale)

        val config = Configuration()
        config.setLocale(locale)
        config.setLayoutDirection(locale)

        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            context.createConfigurationContext(config)
        } else {
            @Suppress("DEPRECATION")
            context.resources.updateConfiguration(config, context.resources.displayMetrics)
            context
        }
    }

    @SuppressLint("ConflictingOnColor")
    fun applySavedLocale(context: Context): Context {
        val prefs = context.getSharedPreferences("AppPreferences", Context.MODE_PRIVATE)
        val langCode = prefs.getString("AppLanguage", "en") ?: "en"
        return setLocale(context, langCode)
    }
}