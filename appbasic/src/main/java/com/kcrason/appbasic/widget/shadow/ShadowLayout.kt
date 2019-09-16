package com.kcrason.appbasic.widget.shadow


import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.drawable.BitmapDrawable
import android.os.Build
import android.util.AttributeSet
import android.widget.FrameLayout
import com.kcrason.appbasic.R
import org.jetbrains.anko.dip
import kotlin.math.abs


class ShadowLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private var mBackGroundColor: Int = 0
    private var mShadowColor: Int = 0
    private var shadowLimit: Float = 0f
    private var cornerRadius: Float = 0f
    private var mDx: Float = 0f
    private var mDy: Float = 0f
    private var leftShow: Boolean = false
    private var rightShow: Boolean = false
    private var topShow: Boolean = false
    private var bottomShow: Boolean = false
    private var shadowPaint: Paint? = null
    private var paint: Paint? = null

    private var leftPadding: Int = 0
    private var topPadding: Int = 0
    private var rightPadding: Int = 0
    private var bottomPadding: Int = 0
    //阴影布局子空间区域
    private val rectF = RectF()

    init {
        initAttributes(attrs)
        shadowPaint = Paint()
        shadowPaint!!.isAntiAlias = true
        shadowPaint!!.style = Paint.Style.FILL

        //矩形画笔
        paint = Paint(Paint.ANTI_ALIAS_FLAG)
        paint!!.style = Paint.Style.FILL
        paint!!.color = mBackGroundColor
        setPadding()
    }

    fun setMDx(mDx: Float) {
        if (abs(mDx) > shadowLimit) {
            if (mDx > 0) {
                this.mDx = shadowLimit
            } else {
                this.mDx = -shadowLimit
            }
        } else {
            this.mDx = mDx
        }
        setPadding()
    }


    fun setMDy(mDy: Float) {
        if (abs(mDy) > shadowLimit) {
            if (mDy > 0) {
                this.mDy = shadowLimit
            } else {
                this.mDy = -shadowLimit
            }
        } else {
            this.mDy = mDy
        }
        setPadding()
    }


    fun setCornerRadius(mCornerRadius: Int) {
        this.cornerRadius = mCornerRadius.toFloat()
        setBackgroundCompat(width, height)
    }

    fun setShadowLimit(mShadowLimit: Int) {
        this.shadowLimit = mShadowLimit.toFloat()
        setPadding()
    }


    fun setShadowColor(mShadowColor: Int) {
        this.mShadowColor = mShadowColor
        setBackgroundCompat(width, height)
    }

    fun setLeftShow(leftShow: Boolean) {
        this.leftShow = leftShow
        setPadding()
    }

    fun setRightShow(rightShow: Boolean) {
        this.rightShow = rightShow
        setPadding()
    }

    fun setTopShow(topShow: Boolean) {
        this.topShow = topShow
        setPadding()
    }

    fun setBottomShow(bottomShow: Boolean) {
        this.bottomShow = bottomShow
        setPadding()
    }


    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        if (w > 0 && h > 0) {
            setBackgroundCompat(w, h)
        }
    }


    private fun setPadding() {
        val xPadding = (shadowLimit + abs(mDx)).toInt()
        val yPadding = (shadowLimit + abs(mDy)).toInt()

        leftPadding = if (leftShow) xPadding else 0
        topPadding = if (topShow) yPadding else 0
        rightPadding = if (rightShow) xPadding else 0
        bottomPadding = if (bottomShow) yPadding else 0

        setPadding(leftPadding, topPadding, rightPadding, bottomPadding)
    }


    private fun setBackgroundCompat(w: Int, h: Int) {
        val bitmap = createShadowBitmap(w, h, cornerRadius, shadowLimit, mDx, mDy, mShadowColor)
        val drawable = BitmapDrawable(bitmap)
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.JELLY_BEAN) {
            setBackgroundDrawable(drawable)
        } else {
            background = drawable
        }
    }


    private fun initAttributes(attrs: AttributeSet?) {
        val attr = context.obtainStyledAttributes(attrs, R.styleable.ShadowLayout)
        //默认是显示
        leftShow = attr.getBoolean(R.styleable.ShadowLayout_hl_leftShow, true)
        rightShow = attr.getBoolean(R.styleable.ShadowLayout_hl_rightShow, true)
        bottomShow = attr.getBoolean(R.styleable.ShadowLayout_hl_bottomShow, true)
        topShow = attr.getBoolean(R.styleable.ShadowLayout_hl_topShow, true)
        cornerRadius = attr.getDimension(R.styleable.ShadowLayout_hl_cornerRadius, 0f)
        //默认扩散区域宽度
        shadowLimit =
            attr.getDimension(R.styleable.ShadowLayout_hl_shadowLimit, context.dip(5).toFloat())

        //x轴偏移量
        mDx = attr.getDimension(R.styleable.ShadowLayout_hl_dx, 0f)
        //y轴偏移量
        mDy = attr.getDimension(R.styleable.ShadowLayout_hl_dy, 0f)
        mShadowColor =
            attr.getColor(R.styleable.ShadowLayout_hl_shadowColor, Color.parseColor("#2a000000"))
        mBackGroundColor =
            attr.getColor(R.styleable.ShadowLayout_hl_shadowBackGroundColor, Color.WHITE)
        attr.recycle()
    }


    private fun createShadowBitmap(
        shadowWidth: Int, shadowHeight: Int, cornerRadius: Float, shadowRadius: Float,
        dx: Float, dy: Float, shadowColor: Int
    ): Bitmap {

        val output = Bitmap.createBitmap(shadowWidth, shadowHeight, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(output)

        val shadowRect = RectF(
            shadowRadius,
            shadowRadius,
            shadowWidth - shadowRadius,
            shadowHeight - shadowRadius
        )

        if (dy > 0) {
            shadowRect.top += dy
            shadowRect.bottom -= dy
        } else if (dy < 0) {
            shadowRect.top += abs(dy)
            shadowRect.bottom -= abs(dy)
        }

        if (dx > 0) {
            shadowRect.left += dx
            shadowRect.right -= dx
        } else if (dx < 0) {
            shadowRect.left += abs(dx)
            shadowRect.right -= abs(dx)
        }


        shadowPaint!!.color = Color.TRANSPARENT
        if (!isInEditMode) {
            shadowPaint!!.setShadowLayer(shadowRadius, dx, dy, shadowColor)
        }

        canvas.drawRoundRect(shadowRect, cornerRadius, cornerRadius, shadowPaint!!)
        return output
    }


    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        rectF.left = leftPadding.toFloat()
        rectF.top = topPadding.toFloat()
        rectF.right = (width - rightPadding).toFloat()
        rectF.bottom = (height - bottomPadding).toFloat()
        val trueHeight = (rectF.bottom - rectF.top).toInt()
        if (cornerRadius > trueHeight shr 1) {
            //画圆角矩形
            canvas.drawRoundRect(
                rectF,
                (trueHeight / 2).toFloat(),
                (trueHeight / 2).toFloat(),
                paint!!
            )
        } else {
            canvas.drawRoundRect(rectF, cornerRadius, cornerRadius, paint!!)
        }
    }

}

