package com.kcrason.appbasic.common

import android.app.Activity
import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.kcrason.appbasic.widget.popupwindow.MultiStatusTip

/**
 * @author KCrason
 * @date 2019/6/27 14:15
 * @description
 */

/**
 * Context扩展函数：Context下添加请求权限方法
 */
fun Context?.fastPermissions(
    permissions: Array<String>,
    autoPromptWhenFailure: Boolean,
    onResultRequestPermissionsListener: ((granted: Boolean) -> Unit)
) {
    if (this is FragmentActivity) {
        PermissionsRequest.generalRequestPermissions(
            this,
            permissions,
            onResultRequestPermissionsListener,
            autoPromptWhenFailure
        )
    } else {
        onResultRequestPermissionsListener.invoke(false)
        MultiStatusTip.showWarnTip(this, "获取权限失败")
    }
}


/**
 * Context扩展函数：Context下添加请求权限方法
 */
fun Context?.fastPermissions(
    permissions: Array<String>,
    onResultRequestPermissionsListener: ((granted: Boolean) -> Unit)
) {
    fastPermissions(permissions, true, onResultRequestPermissionsListener)
}


/**
 * Activity扩展函数：Context下添加请求权限方法
 */
fun Activity?.fastPermissions(
    permissions: Array<String>, autoPromptWhenFailure: Boolean,
    onResultRequestPermissionsListener: ((granted: Boolean) -> Unit)
) {
    if (this is FragmentActivity) {
        PermissionsRequest.generalRequestPermissions(
            this,
            permissions,
            onResultRequestPermissionsListener,
            autoPromptWhenFailure
        )
    } else {
        onResultRequestPermissionsListener.invoke(false)
        MultiStatusTip.showWarnTip(this, "获取权限失败")
    }
}

/**
 * Activity扩展函数：Context下添加请求权限方法
 */
fun Activity?.fastPermissions(
    permissions: Array<String>,
    onResultRequestPermissionsListener: ((granted: Boolean) -> Unit)
) {
    fastPermissions(permissions, true, onResultRequestPermissionsListener)
}

/**
 * Activity扩展函数：Context下添加请求权限方法
 */
fun Fragment?.fastPermissions(
    permissions: Array<String>, autoPromptWhenFailure: Boolean,
    onResultRequestPermissionsListener: ((granted: Boolean) -> Unit)
) {
    if (this != null) {
        PermissionsRequest.generalRequestPermissions(
            this,
            permissions,
            onResultRequestPermissionsListener,
            autoPromptWhenFailure
        )
    } else {
        onResultRequestPermissionsListener.invoke(false)
        MultiStatusTip.showWarnTip(this, "获取权限失败")
    }
}


/**
 * Activity扩展函数：Context下添加请求权限方法
 */
fun Fragment?.fastPermissions(
    permissions: Array<String>,
    onResultRequestPermissionsListener: ((granted: Boolean) -> Unit)
) {
    fastPermissions(permissions, true, onResultRequestPermissionsListener)
}