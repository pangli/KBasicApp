package com.kcrason.appbasic.common.helper

import android.util.Log

object LogHelper {

    private var mDebugMode: Boolean = false

    fun debugMode(debugMode: Boolean) {
        mDebugMode = debugMode
    }

    fun info(message: String) {
        if (mDebugMode) {
            Log.d("BasicModule", message)
        }
    }

    fun tagInfo(tag: String, message: String) {
        if (mDebugMode) {
            Log.d(tag, message)
        }
    }
}