package com.kcrason.appbasic.widget.popupwindow

import android.content.Context
import android.os.Looper
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.kcrason.appbasic.R

/**
 * @author KCrason
 * @date 2019/5/17 21:56
 * @description
 */
object MultiStatusTip {

    /**
     * 弹出一个普通提示
     */
    fun showWarnTip(context: Context?, message: String) = showTip(context, message, TipState.WARN)

    /**
     * 弹出一个普通提示
     */
    fun showCommonTip(context: Context?, message: String) = showTip(context, message, TipState.COMMON)

    private fun showTip(context: Context?, message: String, tipState: TipState) {
        if (context == null) {
            return
        }
        if (Thread.currentThread() == Looper.getMainLooper().thread) {
            realShowTip(context, message, tipState)
        } else {
            Looper.prepare()
            realShowTip(context, message, tipState)
            Looper.loop()
        }
    }


    private fun realShowTip(context: Context?, message: String, tipState: TipState) {
        val toast = Toast(context)
        val toastView = LayoutInflater.from(context).inflate(R.layout.dialog_multi_status_tip_view, null)
        val imageStatus = toastView.findViewById<ImageView>(R.id.imgStatus)
        if (tipState == TipState.COMMON) {
            imageStatus.visibility = View.GONE
        } else {
            imageStatus.visibility = View.VISIBLE
            imageStatus.setImageResource(
                when (tipState) {
                    TipState.WARN -> R.drawable.svg_ic_icon_multi_tip_warn
                    else -> 0
                }
            )
        }
        toastView.findViewById<TextView>(R.id.txtMessage).text = message
        toast.view = toastView
        toast.duration = Toast.LENGTH_SHORT
        toast.setGravity(Gravity.CENTER, 0, 0)
        toast.show()
    }
}

enum class TipState {
    COMMON,  WARN
}