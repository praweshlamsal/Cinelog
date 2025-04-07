package com.example.cinelog.ui.onboarding

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.cinelog.R
import com.example.cinelog.model.OnboardingPage

class OnboardingAdapter(fa: Fragment, context: Context) : FragmentStateAdapter(fa) {
    private val pages = listOf(
        OnboardingPage(
            title = R.string.title_1,
            description = R.string.desc_1,
            imageRes =readFromAssets("first_page.json", context)
        ),
        OnboardingPage(
            title = R.string.title_2,
            description = R.string.desc_2,
            imageRes =readFromAssets("second_page.json", context)
        ),
        OnboardingPage(
            title = R.string.title_3,
            description = R.string.desc_3,
            imageRes =readFromAssets("third_page.json", context)
        )
    )

    override fun getItemCount(): Int = pages.size

    override fun createFragment(position: Int): Fragment {
        return OnboardingPageFragment.newInstance(pages[position])
    }

    private fun readFromAssets(fileName: String, context: Context): String {
        return context.assets.open(fileName).bufferedReader().use { it.readText() }
    }
}

