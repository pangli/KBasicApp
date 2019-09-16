package com.kcrason.appbasic.widget.imageview

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.drawable.ColorDrawable
import android.util.AttributeSet

import androidx.appcompat.widget.AppCompatImageView
import com.kcrason.appbasic.R

/**
 * Created by chenliu on 2016/8/19.<br></br>
 * 描述：
 *
 */
abstract class AbsRoundImageView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatImageView(context, attrs, defStyleAttr) {

    private var mBitmapPaint: Paint? = null

    /**
     * 图片可视区
     */
    protected var roundPath: Path

    /**
     * 图片边框
     */
    protected var borderPath: Path

    /**
     * 边框宽度
     */
    protected var borderWidth: Float = 0.toFloat()

    /**
     * 边框颜色
     */
    private var borderColor: Int = 0

    private var borderPaint: Paint? = null


    /**
     * 获取图片区域纯颜色Bitmap
     * @return
     */
    private val roundBitmap: Bitmap
        get() {
            val bitmap = Bitmap.createBitmap(
                width, height,
                Bitmap.Config.ARGB_8888
            )
            val canvas = Canvas(bitmap)
            val paint = Paint(Paint.ANTI_ALIAS_FLAG)
            paint.color = Color.WHITE
            canvas.drawPath(roundPath, paint)
            return bitmap
        }

    init {
        val ta = context.obtainStyledAttributes(attrs, R.styleable.AbsRoundImageView)
        borderWidth = ta.getDimension(R.styleable.AbsRoundImageView_riv_borderWidth, 0f)
        borderColor = ta.getColor(R.styleable.AbsRoundImageView_riv_borderColor, 0)
        ta.recycle()
        mBitmapPaint = Paint(Paint.ANTI_ALIAS_FLAG)

        roundPath = Path()
        borderPath = Path()

        borderPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        borderPaint!!.strokeWidth = borderWidth

        scaleType = ScaleType.CENTER_CROP
    }


    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        if (changed) {
            initBorderPath()
            initRoundPath()
        }
    }


    /**
     * 初始化边框Path
     */
    protected abstract fun initBorderPath()

    /**
     * 初始化图片区域Path
     */
    protected abstract fun initRoundPath()

    private fun drawBorder(canvas: Canvas) {
        borderPaint!!.style = Paint.Style.STROKE
        borderPaint!!.color = borderColor
        canvas.drawPath(borderPath, borderPaint!!)
    }

    override fun onDraw(canvas: Canvas) {
        drawImage(canvas)
        drawBorder(canvas)
    }

    private fun drawImage(canvas: Canvas) {
        val drawable = drawable
        if (!isInEditMode && drawable != null) {
            try {
                val bitmap: Bitmap = if (drawable is ColorDrawable) {
                    Bitmap.createBitmap(2, 2, Bitmap.Config.ARGB_8888)
                } else {
                    Bitmap.createBitmap(drawable.intrinsicWidth, drawable.intrinsicHeight, Bitmap.Config.ARGB_8888)
                }
                val drawCanvas = Canvas(bitmap)
                drawable.setBounds(0, 0, drawCanvas.width, drawCanvas.height)
                drawable.draw(drawCanvas)

                val roundBm = roundBitmap
                mBitmapPaint!!.reset()
                mBitmapPaint!!.isFilterBitmap = false
                mBitmapPaint!!.xfermode = xFermode
                drawCanvas.drawBitmap(roundBm, 0f, 0f, mBitmapPaint)
                mBitmapPaint!!.xfermode = null
                canvas.drawBitmap(bitmap, 0f, 0f, mBitmapPaint)
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
    }

    companion object {

        private val xFermode = PorterDuffXfermode(PorterDuff.Mode.DST_IN)
    }

}
























