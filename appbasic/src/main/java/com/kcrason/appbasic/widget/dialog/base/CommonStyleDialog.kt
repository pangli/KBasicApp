package com.kcrason.appbasic.widget.dialog.base

import android.content.Context
import android.view.LayoutInflater
import android.view.WindowManager
import com.kcrason.appbasic.R

/**
 * 自定义样式Dialog
 */
abstract class CommonStyleDialog(context: Context, dialogLayoutId: Int) : BaseDialog(
    context, R.style.CommonDialogStyle, LayoutInflater.from(context)
        .inflate(dialogLayoutId, null)
) {
    override fun dialogHeight(): Int = WindowManager.LayoutParams.WRAP_CONTENT
    override fun dialogWidth(): Int = (context.resources.displayMetrics.widthPixels * 0.8).toInt()
}