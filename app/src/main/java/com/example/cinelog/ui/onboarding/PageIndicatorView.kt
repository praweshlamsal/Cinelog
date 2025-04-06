package com.example.cinelog.ui.onboarding

import android.content.Context
import android.util.AttributeSet
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import com.example.cinelog.R

class PageIndicatorView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : LinearLayout(context, attrs) {

    private var currentPage = 0
    private val activeDot = ContextCompat.getDrawable(context, R.drawable.dot_active)
    private val inactiveDot = ContextCompat.getDrawable(context, R.drawable.dot_inactive)

    fun setPageCount(count: Int) {
        removeAllViews()
        for (i in 0 until count) {
            addView(ImageView(context).apply {
                layoutParams = LayoutParams(24.dp, 24.dp).apply {
                    setMargins(8.dp, 0, 8.dp, 0)
                }
                setImageDrawable(if (i == currentPage) activeDot else inactiveDot)
            })
        }
    }

    fun setCurrentPage(position: Int) {
        if (position != currentPage) {
            (getChildAt(currentPage) as? ImageView)?.setImageDrawable(inactiveDot)
            currentPage = position
            (getChildAt(currentPage) as? ImageView)?.setImageDrawable(activeDot)
        }
    }

    private val Int.dp: Int get() = (this * resources.displayMetrics.density).toInt()
}