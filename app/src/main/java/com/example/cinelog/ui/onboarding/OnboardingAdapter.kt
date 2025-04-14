package com.example.cinelog.ui.onboarding

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.cinelog.R
import com.example.cinelog.model.OnboardingPage

class OnboardingAdapter(fa: Fragment, private val context: Context) : FragmentStateAdapter(fa) {

    private val pages = listOf(
        OnboardingPage(
            title = R.string.title_1,
            description = R.string.desc_1,
            imageRes = R.drawable.home_screen
        ),
        OnboardingPage(
            title = R.string.title_2,
            description = R.string.desc_2,
            imageRes = R.drawable.shake_device
        ),
        OnboardingPage(
            title = R.string.title_3,
            description = R.string.desc_3,
            imageRes = R.drawable.graph_chart
        ),
        OnboardingPage(
            title = R.string.title_4,
            description = R.string.desc_4,
            imageRes = R.drawable.filter_by_year
        ),
        OnboardingPage(
            title = R.string.title_5,
            description = R.string.desc_5,
            imageRes = R.drawable.save_movie
        ),
        OnboardingPage(
            title = R.string.title_6,
            description = R.string.desc_6,
            imageRes = R.drawable.added_movie
        ),
        OnboardingPage(
            title = R.string.title_7,
            description = R.string.desc_7,
            imageRes = R.drawable.setting
        )


    )

    override fun getItemCount(): Int = pages.size

    override fun createFragment(position: Int): Fragment {
        return OnboardingPageFragment.newInstance(pages[position])
    }
}
