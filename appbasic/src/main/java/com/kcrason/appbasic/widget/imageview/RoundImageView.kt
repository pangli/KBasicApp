package com.kcrason.appbasic.widget.imageview

import android.content.Context
import android.graphics.Path
import android.graphics.RectF
import android.util.AttributeSet
import com.kcrason.appbasic.R
import kotlin.math.min

/**
 * Created by chenliu on 2016/8/19.<br></br>
 * 描述：
 *
 */
class RoundImageView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
    AbsRoundImageView(context, attrs, defStyleAttr) {

    private var leftTopRadius: Float = 0.toFloat()

    private var rightTopRadius: Float = 0.toFloat()

    private var rightBottomRadius: Float = 0.toFloat()

    private var leftBottomRadius: Float = 0.toFloat()

    init {
        val ta = context.obtainStyledAttributes(attrs, R.styleable.RoundImageView)
        leftTopRadius = ta.getDimension(R.styleable.RoundImageView_riv_leftTopRadius, 0f)
        rightTopRadius = ta.getDimension(R.styleable.RoundImageView_riv_rightTopRadius, 0f)
        rightBottomRadius = ta.getDimension(R.styleable.RoundImageView_riv_rightBottomRadius, 0f)
        leftBottomRadius = ta.getDimension(R.styleable.RoundImageView_riv_leftBottomRadius, 0f)
        ta.recycle()
    }

    override fun initRoundPath() {
        roundPath.reset()
        val width = width
        val height = height
        leftTopRadius = min(leftTopRadius, min(width, height) * 0.5f)
        rightTopRadius = min(rightTopRadius, min(width, height) * 0.5f)
        rightBottomRadius = min(rightBottomRadius, min(width, height) * 0.5f)
        leftBottomRadius = min(leftBottomRadius, min(width, height) * 0.5f)

        val rect = RectF(0f, 0f, width.toFloat(), height.toFloat())
        roundPath.addRoundRect(
            rect,
            floatArrayOf(
                leftTopRadius,
                leftTopRadius,
                rightTopRadius,
                rightTopRadius,
                rightBottomRadius,
                rightBottomRadius,
                leftBottomRadius,
                leftBottomRadius
            ),
            Path.Direction.CW
        )
    }

    override fun initBorderPath() {
        borderPath.reset()
        /**
         * 乘以0.5会导致border在圆角处不能包裹原图
         */
        val halfBorderWidth = borderWidth * 0.35f
        val width = width
        val height = height
        leftTopRadius = min(leftTopRadius, min(width, height) * 0.5f)
        rightTopRadius = min(rightTopRadius, min(width, height) * 0.5f)
        rightBottomRadius = min(rightBottomRadius, min(width, height) * 0.5f)
        leftBottomRadius = min(leftBottomRadius, min(width, height) * 0.5f)

        val rect = RectF(
            halfBorderWidth, halfBorderWidth,
            width - halfBorderWidth, height - halfBorderWidth
        )
        borderPath.addRoundRect(
            rect,
            floatArrayOf(
                leftTopRadius,
                leftTopRadius,
                rightTopRadius,
                rightTopRadius,
                rightBottomRadius,
                rightBottomRadius,
                leftBottomRadius,
                leftBottomRadius
            ),
            Path.Direction.CW
        )
    }
}
