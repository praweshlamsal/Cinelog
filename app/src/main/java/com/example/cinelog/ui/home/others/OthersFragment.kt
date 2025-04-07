package com.example.cinelog.ui.home.others

import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import com.example.cinelog.R
import com.example.cinelog.databinding.FragmentOthersBinding
import com.example.cinelog.ui.history.HistoryActivity
import com.example.cinelog.util.SaveThemeSettings

class OthersFragment : Fragment(R.layout.fragment_others) {
    private var _binding: FragmentOthersBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOthersBinding.inflate(inflater, container, false)

        val themeSettings = SaveThemeSettings(requireContext())
        themeSettings.loadThemePreference()

        val sharedPreferences =
            requireContext().getSharedPreferences("AppPreferences", MODE_PRIVATE)
        binding.themeToggle.isChecked = sharedPreferences.getBoolean("DarkMode", false)

        // Theme toggle listener
        binding.themeToggle.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
            themeSettings.saveThemePreference(isChecked)
        }

        // History item click listener
        binding.historyItem.setOnClickListener {
            val intent = Intent(requireContext(), HistoryActivity::class.java)
            startActivity(intent)
        }

        // ✅ Language item click listener
//        binding.languageItem.setOnClickListener {
//            val intent = Intent(Settings.ACTION_LOCALE_SETTINGS)
//            startActivity(intent)
//        }

        // Language selection dialog
        binding.languageItem.setOnClickListener {
            val languages = arrayOf("English", "Español", "Deutsch", "Nepali")
            val languageCodes = arrayOf("en", "es", "de", "np")

            val builder = android.app.AlertDialog.Builder(requireContext())
            builder.setTitle("Choose Language")
            builder.setItems(languages) { _, which ->
                val selectedLanguage = languageCodes[which]

                // Save to SharedPreferences
                val prefs = requireContext().getSharedPreferences("AppPreferences", MODE_PRIVATE)
                prefs.edit().putString("AppLanguage", selectedLanguage).apply()

                // Apply the language
                LanguageHelper.setLocale(requireContext(), selectedLanguage)

                // Restart the activity to apply changes
                requireActivity().recreate()
            }
            builder.show()
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}