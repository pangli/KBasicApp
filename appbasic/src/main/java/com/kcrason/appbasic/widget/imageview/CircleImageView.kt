package com.kcrason.appbasic.widget.imageview

import android.content.Context
import android.graphics.Path
import android.util.AttributeSet
import com.kcrason.appbasic.widget.imageview.AbsRoundImageView
import kotlin.math.max
import kotlin.math.min

/**
 * Created by chenliu on 2016/8/19.<br></br>
 * 描述：
 *
 */
class CircleImageView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
    AbsRoundImageView(context, attrs, defStyleAttr) {

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val width = max(measuredWidth, measuredHeight)
        setMeasuredDimension(width, width)
    }

    override fun initRoundPath() {
        roundPath.reset()
        val width = width
        val height = height
        val cx = width * 0.5f
        val cy = height * 0.5f
        val radius = min(width, height) * 0.5f
        roundPath.addCircle(cx, cy, radius, Path.Direction.CW)
    }

    override fun initBorderPath() {
        borderPath.reset()
        val halfBorderWidth = borderWidth * 0.5f
        val width = width
        val height = height
        val cx = width * 0.5f
        val cy = height * 0.5f
        val radius = min(width, height) * 0.5f
        borderPath.addCircle(cx, cy, radius - halfBorderWidth, Path.Direction.CW)
    }
}
