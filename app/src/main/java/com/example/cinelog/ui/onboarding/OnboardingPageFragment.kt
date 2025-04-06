package com.example.cinelog.ui.onboarding

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import com.example.cinelog.R
import com.example.cinelog.databinding.FragmentOnboardingPageBinding
import com.example.cinelog.model.OnboardingPage
import com.google.gson.Gson

class OnboardingPageFragment : Fragment(R.layout.fragment_onboarding_page) {

    private var _binding: FragmentOnboardingPageBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentOnboardingPageBinding.bind(view)

        arguments?.getString("page")?.let { json ->
            val page = Gson().fromJson(json, OnboardingPage::class.java)
            updateUI(page)
        }
    }

    private fun updateUI(page: OnboardingPage) {
        with(binding) {
            title.setText(page.title)
            description.setText(page.description)
            image.setImageResource(page.imageRes)
        }
    }

    companion object {
        fun newInstance(page: OnboardingPage) = OnboardingPageFragment().apply {
            arguments = Bundle().apply {
                putString("page", Gson().toJson(page))
            }
        }
    }
}