package com.kcrason.appbasic.common

import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.kcrason.appbasic.widget.dialog.MessageDialog
import com.kcrason.appbasic.widget.dialog.base.BaseCustomDialog
import com.kcrason.appbasic.widget.popupwindow.MultiStatusTip
import com.kcrason.appbasic.R
import java.util.ArrayList

/**
 * @author KCrason
 * @date 2019/5/23 16:31
 * @description 权限处理
 */

class PermissionsRequest : Fragment() {

    private var mPermissions: Array<String>? = null

//    private var isToSettingPermission: Boolean = false

    private var autoPromptWhenFailure: Boolean = true

    /**
     * 权限请求回调
     */
    private var mOnRequestPermissionsListener: ((granted: Boolean) -> Unit)? = null

    private val shouldRationaleList: List<String>?
        get() {
            mPermissions?.let {
                val shouldRationaleArray = ArrayList<String>()
                for (permission in it) {
                    if (shouldShowRequestPermissionRationale(permission)) {
                        shouldRationaleArray.add(permission)
                    }
                }
                return shouldRationaleArray
            }
            return null
        }


    fun setOnRequestPermissionsListener(onRequestPermissionsListener: ((granted: Boolean) -> Unit)?) {
        this.mOnRequestPermissionsListener = onRequestPermissionsListener
    }

    fun setAutoPromptWhenFailure(autoPromptWhenFailure: Boolean) {
        this.autoPromptWhenFailure = autoPromptWhenFailure
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            mPermissions = arguments!!.getStringArray("key_permission_array")
            mPermissions?.let { checkPermission(*it) }
        }
    }


    /**
     * 请求多个权限
     */
    fun checkPermission(vararg permissions: String) {
        val requestPermissionCount = getRequestPermissionList(*permissions)
        if (requestPermissionCount.isNotEmpty()) {
            requestPermissions(
                requestPermissionCount.toTypedArray(),
                PERMISSION_REQUEST_CODE
            )
        } else {
            mOnRequestPermissionsListener?.invoke(true)
        }
    }

    /**
     * 获取需要去申请权限的权限列表
     */
    private fun getRequestPermissionList(vararg permissions: String): List<String> {
        val requestPermissionCount = ArrayList<String>()
        for (permission in permissions) {
            if (ContextCompat.checkSelfPermission(activity!!, permission) != PackageManager.PERMISSION_GRANTED) {
                requestPermissionCount.add(permission)
            }
        }
        return requestPermissionCount
    }

//    override fun onResume() {
//        super.onResume()
//        //如果是从权限设置界面过来，重新检查权限
//        if (isToSettingPermission) {
//            isToSettingPermission = false
//            mPermissions?.let { checkPermission(*it) }
//        }
//    }


    private fun onSurePermission(isRefuse: Boolean) {
        if (isRefuse) {
//            isToSettingPermission = true
            startActivity(Intent().apply {
                action = "android.settings.APPLICATION_DETAILS_SETTINGS"
                data = Uri.fromParts("package", context?.packageName, null)
            })
        } else {
            mPermissions?.let { checkPermission(*it) }
        }
    }


    private fun showPermissionDialog(isRefuse: Boolean) {
        context.checkContextNotFinishing { safeContext ->
            MessageDialog(safeContext)
                .setMessage(
                    if (isRefuse) safeContext.getString(R.string.permission_setting_message) else
                        safeContext.getString(R.string.permission_authorization_message)
                )
                .setBottomClickViewStyle(
                    BaseCustomDialog.BottomClickViewStyle(safeContext)
                        .setLeftViewText(safeContext.getString(R.string.refuse)).setLeftViewColor(Color.RED).setRightViewText(
                            if (isRefuse) safeContext.getString(R.string.setting) else safeContext.getString(
                                R.string.authorization
                            )
                        )
                )
                .setCommonDialogLeftClickListener {
                    //取消
                    MultiStatusTip.showWarnTip(context, "请打开相应的权限")
                    mOnRequestPermissionsListener?.invoke(false)
                }
                .setCommonDialogRightClickListener {
                    //确定
                    onSurePermission(isRefuse)
                }.show()
        }
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQUEST_CODE && grantResults.isNotEmpty()) {
            val failurePermissionCount = ArrayList<Int>()
            for (grantResult in grantResults) {
                if (grantResult != PackageManager.PERMISSION_GRANTED) {
                    failurePermissionCount.add(grantResult)
                }
            }
            if (failurePermissionCount.size == 0) {
                //全部成功
                mOnRequestPermissionsListener?.invoke(true)
            } else {
                //失败
                if (autoPromptWhenFailure) {
                    val rationaleList = shouldRationaleList
                    if (rationaleList.isNullOrEmpty()) {
                        showPermissionDialog(true)
                    } else {
                        showPermissionDialog(false)
                    }
                } else {
                    mOnRequestPermissionsListener?.invoke(false)
                }
            }
        }
    }

    companion object {

        private fun localFindFragmentByTag(fragment: Fragment): Fragment? {
            return fragment.childFragmentManager.findFragmentByTag("PermissionsRequest")
        }

        private fun localFindFragmentByTag(activity: FragmentActivity): Fragment? {
            return activity.supportFragmentManager.findFragmentByTag("PermissionsRequest")
        }

        fun generalRequestPermissions(
            fragment: Fragment,
            permissions: Array<String>, onResultRequestPermissionsListener: ((granted: Boolean) -> Unit),autoPromptWhenFailure: Boolean
        ) {
            val localFragment = localFindFragmentByTag(fragment)
            if (localFragment == null) {
                fragment.childFragmentManager.beginTransaction().add(PermissionsRequest().apply {
                    arguments = Bundle().apply { putStringArray("key_permission_array", permissions) }
                    setOnRequestPermissionsListener(onResultRequestPermissionsListener)
                    setAutoPromptWhenFailure(autoPromptWhenFailure)
                }, "PermissionsRequest").commit()
            } else {
                if (localFragment is PermissionsRequest) {
                    localFragment.setOnRequestPermissionsListener(onResultRequestPermissionsListener)
                    localFragment.setAutoPromptWhenFailure(autoPromptWhenFailure)
                    localFragment.checkPermission(*permissions)
                }
            }
        }

        fun generalRequestPermissions(
            activity: FragmentActivity,
            permissions: Array<String>,
            onResultRequestPermissionsListener: ((granted: Boolean) -> Unit), autoPromptWhenFailure: Boolean
            ) {
            val localFragment = localFindFragmentByTag(activity)
            if (localFragment == null) {
                activity.supportFragmentManager?.beginTransaction()
                    ?.add(PermissionsRequest().apply {
                        arguments = Bundle().apply { putStringArray("key_permission_array", permissions) }
                        setOnRequestPermissionsListener(onResultRequestPermissionsListener)
                        setAutoPromptWhenFailure(autoPromptWhenFailure)
                    }, "PermissionsRequest")
                    ?.commit()
            } else {
                if (localFragment is PermissionsRequest) {
                    localFragment.setOnRequestPermissionsListener(onResultRequestPermissionsListener)
                    localFragment.setAutoPromptWhenFailure(autoPromptWhenFailure)
                    localFragment.checkPermission(*permissions)
                }
            }
        }

        const val PERMISSION_REQUEST_CODE = 0x01
    }
}


