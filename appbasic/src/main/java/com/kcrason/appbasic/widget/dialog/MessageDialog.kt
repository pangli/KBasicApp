package com.kcrason.appbasic.widget.dialog

import android.content.Context
import android.view.View
import androidx.core.content.ContextCompat
import com.kcrason.appbasic.R
import com.kcrason.appbasic.widget.dialog.base.BaseCustomDialog
import kotlinx.android.synthetic.main.dialog_message_view.view.*
import kotlinx.android.synthetic.main.dialog_multi_status_tip_view.view.txtMessage

/**
 * 普通的消息通知Dialog
 */
class MessageDialog(context: Context) : BaseCustomDialog(context, R.layout.dialog_message_view) {

    fun setTitleStyle(titleStyle: TitleStyle): MessageDialog {
        containerView.txtTitle.visibility = titleStyle.titleVisible
        containerView.txtTitle.setTextColor(titleStyle.titleColor)
        return this
    }

    fun setTitle(title: String): MessageDialog {
        containerView.txtTitle.text = title
        return this
    }

    fun setMessage(message: String): MessageDialog {
        containerView.txtMessage.text = message
        return this
    }

    class TitleStyle(context: Context) {
        var titleVisible: Int = View.VISIBLE
        var titleColor: Int = ContextCompat.getColor(context, R.color.color_3a445f)

        fun setTitleColor(titleColor: Int): TitleStyle {
            this.titleColor = titleColor
            return this
        }

        fun setTitleVisible(visibility: Int): TitleStyle {
            titleVisible = visibility
            return this
        }
    }
}