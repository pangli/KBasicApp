package com.kcrason.appbasic.listeners

/**
 * @author KCrason
 * @date 2019/9/3 16:18
 * @description
 */
interface OnSoftKeyboardStateListener {
    fun onSoftKeyboardOpened(keyboardHeightInPx: Int)

    fun onSoftKeyboardClosed()
}