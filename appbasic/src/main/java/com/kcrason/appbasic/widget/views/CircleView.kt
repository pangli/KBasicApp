package com.kcrason.appbasic.widget.views

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import com.kcrason.appbasic.R

/**
 * @author KCrason
 * @date 2019/5/23 16:27
 * @description
 */
class CircleView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val mBackgroundPaint = Paint()

    init {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.CircleView)
        mBackgroundPaint.color = typedArray.getColor(
            R.styleable.CircleView_circle_view_color,
            ContextCompat.getColor(context, R.color.color_0084ff)
        )
        typedArray.recycle()
        mBackgroundPaint.isAntiAlias = true
        mBackgroundPaint.style = Paint.Style.FILL
    }


    fun setColor(color: Int) {
        mBackgroundPaint.color = color
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        val width = width
        canvas.drawCircle((width / 2).toFloat(), (width / 2).toFloat(), (width / 2).toFloat(), mBackgroundPaint)
    }
}
