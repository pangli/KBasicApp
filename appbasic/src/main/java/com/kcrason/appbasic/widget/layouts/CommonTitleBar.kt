package com.kcrason.appbasic.widget.layouts

import android.app.Activity
import android.content.Context
import android.text.TextUtils
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.core.content.ContextCompat
import com.kcrason.appbasic.R
import kotlinx.android.synthetic.main.view_common_title_bar.view.*

/**
 * @author KCrason
 * @date 2019/5/23 16:27
 * @description  共有的titleBar
 */
class CommonTitleBar @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {


    init {
        init(context, attrs)
    }


    fun setTitleBarBackgroundColor(barBackgroundColor: Int): CommonTitleBar {
        layoutTitleBar.setBackgroundColor(barBackgroundColor)
        return this
    }

    fun setLeftIconVisible(visibility: Int): CommonTitleBar {
        imgLeftIcon.visibility = visibility
        return this
    }

    /**
     * 设置左边imageView的图标
     *
     * @param resource drawable id
     */
    fun setLeftIcon(resource: Int): CommonTitleBar {
        imgLeftIcon.setImageResource(resource)
        return this
    }

    /**
     * 设置中间TextView的文字
     *
     * @param centerText 标题
     */
    fun setCenterText(centerText: String?): CommonTitleBar {
        txtCenterText.text = centerText
        return this
    }

    /**
     * 设置中间TextView的文字颜色
     *
     * @param centerTextColor 颜色
     */
    fun setCenterTextColor(centerTextColor: Int): CommonTitleBar {
        txtCenterText.setTextColor(centerTextColor)
        return this
    }

    /**
     * 获取中间TextView
     */
    fun getCenterTextView(): TextView {
        return txtCenterText
    }


    /**
     * 设置中间子TextView的文字
     *
     * @param subCenterText 子标题
     */
    fun setSubCenterText(subCenterText: String?): CommonTitleBar {
        txtSubCenterText.visibility = View.VISIBLE
        txtSubCenterText.text = subCenterText
        return this
    }

    /**
     * 设置中间子TextView的文字颜色
     *
     * @param subCenterTextColor 颜色
     */
    fun setSubCenterTextColor(subCenterTextColor: Int): CommonTitleBar {
        txtSubCenterText.setTextColor(subCenterTextColor)
        return this
    }

    /**
     * 获取中间子TextView
     */
    fun getSubCenterTextView(): TextView {
        return txtSubCenterText
    }

    /**
     * 设置右边的TextView是否显示
     */
    fun setRightTextVisible(visibility: Int): CommonTitleBar {
        txtRightText.visibility = visibility
        return this
    }

    /**
     * 设置右边的TextView的文字
     */
    fun setRightText(rightText: String): CommonTitleBar {
        txtRightText.visibility = View.VISIBLE
        txtRightText.text = rightText
        return this
    }

    /**
     * 设置右边textView的文字颜色
     */
    fun setRightTextColor(rightTextColor: Int): CommonTitleBar {
        txtRightText.setTextColor(rightTextColor)
        return this
    }


    /**
     * 设置左边imageView的点击
     */
    fun setLeftIconClickListener(leftIconClickListener: OnClickListener): CommonTitleBar {
        imgLeftIcon.setOnClickListener(leftIconClickListener)
        return this
    }


    /**
     * 设置右边TextView的点击
     */
    fun setRightTextClickListener(rightTextClickListener: OnClickListener): CommonTitleBar {
        txtRightText.setOnClickListener(rightTextClickListener)
        return this
    }

    /**
     * 设置右边icon点击
     */
    fun setRightIconClickListener(rightIconClickListener: OnClickListener): CommonTitleBar {
        imgRightIcon.visibility = View.VISIBLE
        imgRightIcon.setOnClickListener(rightIconClickListener)
        return this
    }

    /**
     * 设置右边icon
     */
    fun setRightIcon(rightIcon: Int): CommonTitleBar {
        imgRightIcon.setImageResource(rightIcon)
        return this
    }

    private fun init(context: Context, attributeSet: AttributeSet?) {
        LayoutInflater.from(context).inflate(R.layout.view_common_title_bar, this, true)
        imgLeftIcon.setOnClickListener {
            if (context is Activity) {
                context.finish()
            }
        }

        val typedArray = context.obtainStyledAttributes(attributeSet, R.styleable.CommonTitleBar)


        val titleBarBackGroundColor = typedArray.getColor(
            R.styleable.CommonTitleBar_backgroundColor,
            ContextCompat.getColor(context, R.color.colorPrimary)
        )

        layoutTitleBar.setBackgroundColor(titleBarBackGroundColor)

        val leftIconVisible = typedArray.getInt(R.styleable.CommonTitleBar_leftIconVisible, 0)

        imgLeftIcon.visibility = if (leftIconVisible == 0) View.VISIBLE else View.GONE

        imgLeftIcon.setImageResource(
            typedArray.getResourceId(
                R.styleable.CommonTitleBar_leftIcon,
                R.drawable.svg_ic_icon_common_back_normal
            )
        )

        val centerText = typedArray.getString(R.styleable.CommonTitleBar_centerText)
        txtCenterText.text = centerText
        txtCenterText.setTextColor(
            typedArray.getColor(
                R.styleable.CommonTitleBar_centerTextColor,
                ContextCompat.getColor(context, R.color.color_ffffff)
            )
        )


        val subCenterText = typedArray.getString(R.styleable.CommonTitleBar_subCenterText)
        txtSubCenterText.visibility = if (subCenterText.isNullOrEmpty()) View.GONE else View.VISIBLE
        txtSubCenterText.text = subCenterText
        txtSubCenterText.setTextColor(
            typedArray.getColor(
                R.styleable.CommonTitleBar_subCenterTextColor,
                ContextCompat.getColor(context, R.color.color_ffffff)
            )
        )

        val rightIcon = typedArray.getResourceId(R.styleable.CommonTitleBar_rightIcon, 0)
        if (rightIcon == 0) {
            imgRightIcon.visibility = View.GONE
        } else {
            imgRightIcon.visibility = View.VISIBLE
            imgRightIcon.setImageResource(rightIcon)
        }

        val rightText = typedArray.getString(R.styleable.CommonTitleBar_rightText)
        txtRightText.visibility = if (TextUtils.isEmpty(rightText)) View.GONE else View.VISIBLE
        txtRightText.text = rightText
        txtRightText.setTextColor(
            typedArray.getColor(
                R.styleable.CommonTitleBar_rightTextColor,
                ContextCompat.getColor(context, R.color.color_ffffff)
            )
        )
        typedArray.recycle()
    }
}
