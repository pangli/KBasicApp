package com.kcrason.appbasic.widget.dialog.base

import android.app.Dialog
import android.content.Context
import android.view.*

/**
 * 自定义dialog
 */
abstract class BaseDialog(context: Context, themeResId: Int = 0, dialogView: View) :
    Dialog(context, themeResId) {

    val mContext = context

    var containerView: View = dialogView

    private var mDialogParams: WindowManager.LayoutParams? = null

    init {
        initBaseDialog(containerView)
    }

    fun setOutCancelable(cancelable: Boolean): BaseDialog {
        setCancelable(cancelable)
        return this
    }

    fun setDimAmount(dimAmount: Float): BaseDialog {
        mDialogParams?.dimAmount = dimAmount
        return this
    }

    fun setDialogGravity(dialogGravity: Int): BaseDialog {
        mDialogParams?.gravity = dialogGravity
        return this
    }

    fun setWindowAnimations(windowAnimations: Int): BaseDialog {
        window?.setWindowAnimations(windowAnimations)
        return this
    }

    fun setOutCanceledOnTouchOutside(canceledOnTouchOutside: Boolean): BaseDialog {
        setCanceledOnTouchOutside(canceledOnTouchOutside)
        return this
    }

    private fun initBaseDialog(containerView: View) {
        setContentView(containerView)
        this.mDialogParams = window?.attributes
        initDialog()
    }

    override fun show() {
        mDialogParams?.let {
            it.width = dialogWidth()
            it.height = dialogHeight()
            window?.attributes = it
        }
        super.show()
    }

    protected abstract fun dialogWidth(): Int

    protected abstract fun dialogHeight(): Int

    protected abstract fun initDialog()
}