package com.kcrason.appbasic.common

import android.app.Activity
import android.os.Build
import android.view.View

/**
 * @author KCrason
 * @date 2019/8/28 15:24
 * @description
 */


/**
 * Activity扩展函数：标记状态栏颜色为黑色风格
 */
fun Activity?.systemUIFlagLightStatusBar() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) run {
        this?.window?.decorView?.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
    }
}


