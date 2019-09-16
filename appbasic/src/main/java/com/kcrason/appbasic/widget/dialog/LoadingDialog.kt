package com.kcrason.appbasic.widget.dialog

import android.content.Context
import android.view.WindowManager
import com.kcrason.appbasic.R
import com.kcrason.appbasic.widget.dialog.base.CommonStyleDialog
import kotlinx.android.synthetic.main.dialog_loading_view.view.*

class LoadingDialog(context: Context) : CommonStyleDialog(
    context, R.layout.dialog_loading_view
) {

    fun setMessage(message: String?): LoadingDialog {
        containerView.txtMessage.text = message
        return this
    }

    override fun dialogWidth(): Int {
        return WindowManager.LayoutParams.WRAP_CONTENT
    }

    override fun initDialog() {
        setWindowAnimations(0)
        setDimAmount(0f)
        setOutCanceledOnTouchOutside(false)
    }
}