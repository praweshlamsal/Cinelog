package com.example.cinelog.ui.onboarding

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.cinelog.R
import com.example.cinelog.model.OnboardingPage

class OnboardingAdapter(fa: Fragment) : FragmentStateAdapter(fa) {
    private val pages = listOf(
        OnboardingPage(
            title = R.string.title_1,
            description = R.string.desc_1,
            imageRes = R.drawable.ic_onboarding1
        ),
        OnboardingPage(
            title = R.string.title_2,
            description = R.string.desc_2,
            imageRes = R.drawable.ic_onboarding2
        ),
        OnboardingPage(
            title = R.string.title_3,
            description = R.string.desc_3,
            imageRes = R.drawable.ic_onboarding3
        )
    )

    override fun getItemCount(): Int = pages.size

    override fun createFragment(position: Int): Fragment {
        return OnboardingPageFragment.newInstance(pages[position])
    }
}

