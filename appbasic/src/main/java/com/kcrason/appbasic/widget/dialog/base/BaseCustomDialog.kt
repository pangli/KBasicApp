package com.kcrason.appbasic.widget.dialog.base

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import androidx.core.content.ContextCompat
import com.kcrason.appbasic.R
import kotlinx.android.synthetic.main.dialog_common_view.view.*


/**
 * 自定义dialog基类，同一style下的其他所有dialog继承该类
 */
abstract class BaseCustomDialog(context: Context, childLayoutId: Int) :
    CommonStyleDialog(context, R.layout.dialog_common_view) {

    private var mCommonDialogLeftClickListener: (() -> Unit)? = null
    private var mCommonDialogRightClickListener: (() -> Unit)? = null
    private var mCommonDialogCenterClickListener: (() -> Unit)? = null

    init {
        containerView.dialogContainer.addView(
            LayoutInflater.from(context).inflate(
                childLayoutId,
                containerView.dialogContainer,
                false
            )
        )
    }

    fun setCommonDialogCenterClickListener(onCommonDialogCenterClickListener: (() -> Unit)?): BaseCustomDialog {
        this.mCommonDialogCenterClickListener = onCommonDialogCenterClickListener
        return this
    }

    fun setCommonDialogLeftClickListener(onCommonDialogLeftClickListener: (() -> Unit)?): BaseCustomDialog {
        this.mCommonDialogLeftClickListener = onCommonDialogLeftClickListener
        return this
    }

    fun setCommonDialogRightClickListener(onCommonDialogRightClickListener: (() -> Unit)?): BaseCustomDialog {
        this.mCommonDialogRightClickListener = onCommonDialogRightClickListener
        return this
    }


    fun setBottomClickViewStyle(bottomClickViewStyle: BottomClickViewStyle): BaseCustomDialog {
        containerView.txt_left.apply {
            setTextColor(bottomClickViewStyle.leftViewColor)
            text = bottomClickViewStyle.leftViewText
        }
        containerView.txt_center.apply {
            setTextColor(bottomClickViewStyle.centerViewColor)
            text = bottomClickViewStyle.centerViewText
            visibility = if (bottomClickViewStyle.showCenterClickView) View.VISIBLE else View.GONE
        }
        containerView.txt_right.apply {
            setTextColor(bottomClickViewStyle.rightViewColor)
            text = bottomClickViewStyle.rightViewText
        }
        return this
    }

    override fun initDialog() {
        containerView.txt_left.setOnClickListener {
            run {
                mCommonDialogLeftClickListener?.invoke()
                dismiss()
            }
        }
        containerView.txt_right.setOnClickListener {
            run {
                mCommonDialogRightClickListener?.invoke()
                dismiss()
            }
        }
        containerView.txt_center.setOnClickListener {
            run {
                mCommonDialogCenterClickListener?.invoke()
                dismiss()
            }
        }
    }


    class BottomClickViewStyle(context: Context) {

        var showCenterClickView: Boolean = false

        /**
         * 是否只显示一个确认按钮，该按钮水平铺满，默认显示左右两个按钮风格
         */
        fun showCenterClickView(showCenterClickView: Boolean): BottomClickViewStyle {
            this.showCenterClickView = showCenterClickView
            return this
        }

        /**
         * 修改左边按钮文字内容
         */
        var leftViewText: String = context.getString(R.string.cancel)

        fun setLeftViewText(leftViewText: String): BottomClickViewStyle {
            this.leftViewText = leftViewText
            return this
        }

        /**
         * 修改左边按钮文字颜色
         */
        var leftViewColor: Int = ContextCompat.getColor(context, R.color.color_0084ff)

        fun setLeftViewColor(leftViewColor: Int): BottomClickViewStyle {
            this.leftViewColor = leftViewColor
            return this
        }

        /**
         * 修改右边按钮文字内容
         */
        var rightViewText: String = context.getString(R.string.determine)


        fun setRightViewText(rightViewText: String): BottomClickViewStyle {
            this.rightViewText = rightViewText
            return this
        }

        /**
         * 修改右边按钮文字颜色
         */
        var rightViewColor: Int = ContextCompat.getColor(context, R.color.color_ffffff)

        fun setRightViewColor(rightViewColor: Int): BottomClickViewStyle {
            this.rightViewColor = rightViewColor
            return this
        }

        /**
         * 修改铺满按钮文字内容
         */
        var centerViewText: String = context.getString(R.string.i_know)

        fun setCenterViewText(centerViewText: String): BottomClickViewStyle {
            this.centerViewText = centerViewText
            return this
        }


        /**
         * 修改铺满按钮文字颜色
         */
        var centerViewColor: Int = ContextCompat.getColor(context, R.color.color_ffffff)

        fun setCenterViewColor(centerViewColor: Int): BottomClickViewStyle {
            this.centerViewColor = centerViewColor
            return this
        }
    }
}
