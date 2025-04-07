package com.example.cinelog.ui.onboarding

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.example.cinelog.R
import com.example.cinelog.data.local.sharedPref.SharedPrefHelper
import com.example.cinelog.databinding.FragmentOnboardingBinding
import com.example.cinelog.ui.home.MainActivity
import com.example.cinelog.viewModel.OnboardingViewModel

class OnboardingFragment: Fragment(R.layout.fragment_onboarding) {
    private lateinit var binding: FragmentOnboardingBinding
    private lateinit var viewModel: OnboardingViewModel
    private lateinit var sharedPrefHelper: SharedPrefHelper

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentOnboardingBinding.bind(view)
        sharedPrefHelper = SharedPrefHelper(requireContext())

        viewModel = ViewModelProvider(this)[OnboardingViewModel::class.java]
        setupViewPager()
        setupObservers()
        setupClickListeners()
    }

    private fun setupViewPager() {
        binding.viewPager.adapter = OnboardingAdapter(this, requireContext())
        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                viewModel.updatePage(position)
            }
        })
        binding.indicator.setPageCount(3)
        binding.indicator.setCurrentPage(0)
    }

    private fun setupObservers() {
        viewModel.currentPage.observe(viewLifecycleOwner) { position ->
            updateUI(position)
        }

        viewModel.navigationEvent.observe(viewLifecycleOwner) { shouldNavigate ->
            if (shouldNavigate) {
                sharedPrefHelper.isOnboardingCompleted = true
                (activity as? MainActivity)?.setupBottomNavigation()
                findNavController().navigate(R.id.action_onboarding_to_main)
            }
        }
    }

    private fun setupClickListeners() {
        binding.btnNext.setOnClickListener {
            val nextPage = viewModel.getNextPage()
            binding.viewPager.currentItem = nextPage as Int

        }
        binding.btnSkip.setOnClickListener { viewModel.completeOnboarding() }
    }

    private fun updateUI(position: Int) {
        with(binding) {
            btnNext.text = if (position == 2) "Get Started" else "Next"
            btnSkip.visibility = if (position == 2) View.GONE else View.VISIBLE
            indicator.setCurrentPage(position)
        }
    }
}