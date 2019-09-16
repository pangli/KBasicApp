package com.kcrason.appbasic.widget.layouts

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import android.util.TypedValue
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import com.kcrason.appbasic.R
import com.kcrason.appbasic.common.enums.FastSortType
import kotlinx.android.synthetic.main.view_fast_sort.view.*
import org.jetbrains.anko.dip

/**
 * @author KCrason
 * @date 2019/6/12 15:16
 * @description
 */
class FastSortView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private var fastSortType: FastSortType = FastSortType.DEFAULT

    private var mOnFastSortListener: ((fastSortType: FastSortType) -> Unit)? = null

    fun setFastSortListener(onFastSortListener: ((fastSortType: FastSortType) -> Unit)?) {
        this.mOnFastSortListener = onFastSortListener
    }

    fun setFastSortName(fastSortName: String) {
        txtSortName.text = fastSortName
    }

    init {
        LayoutInflater.from(context).inflate(R.layout.view_fast_sort, this, true)
        val typeArray = context.obtainStyledAttributes(attrs, R.styleable.FastSortView)
        val nameColor = typeArray.getColor(
            R.styleable.FastSortView_fsvNameColor,
            ContextCompat.getColor(context, R.color.color_707070)
        )
        val nameSize = typeArray.getDimension(R.styleable.FastSortView_fsvNameSize, context.dip(14).toFloat())
        val nameStyle = typeArray.getInt(R.styleable.FastSortView_fsvNameFontStyle, 2)

        txtSortName.text = typeArray.getString(R.styleable.FastSortView_fsvName)
        txtSortName.setTextColor(nameColor)
        txtSortName.setTextSize(TypedValue.COMPLEX_UNIT_PX, nameSize)
        txtSortName.typeface = if (nameStyle == 1) Typeface.DEFAULT else Typeface.DEFAULT_BOLD
        typeArray.recycle()


        layoutFastSort.setOnClickListener {
            when (fastSortType) {
                FastSortType.DEFAULT -> {
                    fastSortType = FastSortType.UP
                    imgSortState.setImageResource(R.drawable.svg_ic_icon_common_filter_up_normal)
                }
                FastSortType.UP -> {
                    fastSortType = FastSortType.DOWN
                    imgSortState.setImageResource(R.drawable.svg_ic_icon_common_filter_down_normal)
                }
                FastSortType.DOWN -> {
                    fastSortType = FastSortType.DEFAULT
                    imgSortState.setImageResource(R.drawable.svg_ic_icon_common_filter_normal)
                }
            }
            mOnFastSortListener?.invoke(fastSortType)
        }
    }
}