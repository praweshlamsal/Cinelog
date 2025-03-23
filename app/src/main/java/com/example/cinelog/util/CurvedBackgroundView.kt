package com.example.cinelog.util

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.View
import androidx.core.content.res.ResourcesCompat

class CurvedBackgroundView(context: Context, attrs: AttributeSet?) : View(context, attrs) {

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        color = ResourcesCompat.getColor(resources, android.R.color.transparent, context.theme)
    }

    private val path = Path()

    init {
        val typedArray = context.obtainStyledAttributes(attrs, intArrayOf(android.R.attr.colorPrimary), 0, 0)
        paint.color = typedArray.getColor(0, paint.color)
        typedArray.recycle()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val width = width.toFloat()
        val height = height.toFloat()
        val curveHeight = height * 0.3f

        path.reset()
        path.moveTo(0f, 0f)
        path.lineTo(width, 0f)
        path.lineTo(width, height - curveHeight)
        path.quadTo(width / 2, height, 0f, height - curveHeight)
        path.close()

        canvas.drawPath(path, paint)
    }
}