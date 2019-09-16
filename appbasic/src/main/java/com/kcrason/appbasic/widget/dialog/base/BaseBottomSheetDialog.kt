package com.kcrason.appbasic.widget.dialog.base

import android.content.Context
import android.view.View
import com.google.android.material.bottomsheet.BottomSheetDialog
import android.view.ViewGroup
import android.os.Bundle
import com.kcrason.appbasic.R


/**
 * @author KCrason
 * @date 2019/5/24 9:51
 * @description
 */
abstract class BaseBottomSheetDialog(context: Context, containerView: View) :
    BottomSheetDialog(context, R.style.CommonDialogStyle) {

    val mContainerView: View = containerView

    init {
        this.setContentView(containerView)
        this.initSheetDialog()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val screenHeight = context.resources.displayMetrics.heightPixels
        window?.run {
            setWindowAnimations(R.style.ShareDialogAnimation)
            setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                if (screenHeight == 0) ViewGroup.LayoutParams.MATCH_PARENT else screenHeight
            )
        }
    }

    abstract fun initSheetDialog()
}