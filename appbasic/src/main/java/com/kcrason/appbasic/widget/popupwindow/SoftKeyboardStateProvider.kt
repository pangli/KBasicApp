package com.kcrason.appbasic.widget.popupwindow

import android.view.Gravity
import android.widget.PopupWindow
import android.view.WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE
import android.graphics.drawable.ColorDrawable
import android.app.Activity
import android.graphics.Rect
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import com.kcrason.appbasic.listeners.OnSoftKeyboardStateListener

/**
 * @author KCrason
 * @date 2019/9/3 15:15
 * @description
 * 用于在 windowSoftInputMode = adjustNothing模式下的键盘状态检测
 */
class SoftKeyboardStateProvider(private val mActivity: Activity) : PopupWindow(mActivity),
    OnGlobalLayoutListener {
    private val rootView: View = View(mActivity)
    private var heightMax: Int = 0
    private var isSoftKeyboardOpened: Boolean = false

    private var mOnSoftKeyboardStateListener: OnSoftKeyboardStateListener? = null

    init {
        this.contentView = rootView
        rootView.viewTreeObserver.addOnGlobalLayoutListener(this)
        setBackgroundDrawable(ColorDrawable(0))
        // 设置宽度为0，高度为全屏
        width = 0
        height = ViewGroup.LayoutParams.MATCH_PARENT
        // 设置键盘弹出方式
        softInputMode = SOFT_INPUT_ADJUST_RESIZE
        inputMethodMode = INPUT_METHOD_NEEDED
    }

    fun init(): SoftKeyboardStateProvider {
        if (!isShowing) {
            val view = mActivity.window.decorView
            view.post { showAtLocation(view, Gravity.NO_GRAVITY, 0, 0) }
        }
        return this
    }

    fun setOnKeyBroadHeightListener(onSoftKeyboardStateListener: OnSoftKeyboardStateListener) {
        this.mOnSoftKeyboardStateListener = onSoftKeyboardStateListener
    }


    override fun onGlobalLayout() {
        val rect = Rect()
        rootView.getWindowVisibleDisplayFrame(rect)
        if (rect.bottom > heightMax) {
            heightMax = rect.bottom
        }
        val keyboardHeight = heightMax - rect.bottom
        if (!isSoftKeyboardOpened && keyboardHeight > 200) {
            isSoftKeyboardOpened = true
            mOnSoftKeyboardStateListener?.onSoftKeyboardOpened(keyboardHeight)
        } else if (isSoftKeyboardOpened && keyboardHeight < 200) {
            isSoftKeyboardOpened = false
            mOnSoftKeyboardStateListener?.onSoftKeyboardClosed()
        }
    }


}