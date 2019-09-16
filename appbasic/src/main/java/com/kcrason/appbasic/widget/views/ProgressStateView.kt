package com.kcrason.appbasic.widget.views

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import com.kcrason.appbasic.R
import org.jetbrains.anko.dip

/**
 * @author KCrason
 * @date 2019/8/28 11:41
 * @description
 */
class ProgressStateView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var mOutRingPaint: Paint = Paint()

    private var mProgressPaint = Paint()

    private var mBackgroundPaint = Paint()

    private var mOffset = 0f

    private var mRectF = RectF()

    private var end = 0f

    init {
        visibility = GONE
        val typedValue = context.obtainStyledAttributes(attrs, R.styleable.ProgressStateView)

        val outRingColor =
            typedValue.getColor(R.styleable.ProgressStateView_outRingColor, Color.WHITE)
        mOffset = typedValue.getDimension(R.styleable.ProgressStateView_offset, dip(6).toFloat())

        mProgressPaint.isAntiAlias = true
        mProgressPaint.color = typedValue.getColor(
            R.styleable.ProgressStateView_innerCircleBackgroundColor,
            Color.WHITE
        )
        mProgressPaint.style = Paint.Style.FILL

        mBackgroundPaint.isAntiAlias = true
        mBackgroundPaint.color = typedValue.getColor(
            R.styleable.ProgressStateView_circleBackgroundColor,
            Color.parseColor("#88000000")
        )
        mBackgroundPaint.style = Paint.Style.FILL

        mOutRingPaint.isAntiAlias = true
        mOutRingPaint.color = outRingColor
        mOutRingPaint.style = Paint.Style.STROKE
        mOutRingPaint.strokeWidth = dip(2).toFloat()

        typedValue.recycle()
    }

    fun start() {
        end = 0f
        invalidate()
        visibility = VISIBLE
    }

    fun finish() {
        visibility = GONE
    }

    fun update(progress: Int, completeState: Boolean) {
        if (completeState) {
            visibility = GONE
        } else {
            end = (progress / 100f) * 360
            invalidate()
        }
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        val cx = width / 2f
        val cy = height / 2f
        val radius = if (cx > cy) cy else cx

        canvas?.drawCircle(cx, cy, radius, mBackgroundPaint)

        canvas?.drawCircle(
            cx,
            cy,
            radius - mOutRingPaint.strokeWidth / 2,
            mOutRingPaint
        )

        mRectF.left = mOffset
        mRectF.top = mOffset
        mRectF.right = width - mOffset
        mRectF.bottom = height - mOffset

        canvas?.drawArc(mRectF, 0f, end, true, mProgressPaint)
    }
}