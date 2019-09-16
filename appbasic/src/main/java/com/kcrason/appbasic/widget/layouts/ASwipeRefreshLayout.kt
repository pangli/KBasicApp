package com.kcrason.appbasic.widget.layouts

import android.content.Context
import android.util.AttributeSet
import androidx.core.content.ContextCompat
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.kcrason.appbasic.R

/**
 * @author KCrason
 * @date 2019/5/20 18:27
 * @description
 */
class ASwipeRefreshLayout : SwipeRefreshLayout {

    constructor(context: Context) : super(context)

    constructor(context: Context, set: AttributeSet) : super(context, set) {
        setColorSchemeColors(
            ContextCompat.getColor(context, R.color.color_0084ff)
        )
    }
}