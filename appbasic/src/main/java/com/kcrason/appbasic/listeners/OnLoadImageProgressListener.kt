package com.kcrason.appbasic.listeners

import android.graphics.Bitmap

/**
 * @author KCrason
 * @date 2019/8/29 15:13
 * @description
 */
interface OnLoadImageProgressListener {
    fun onLoadStarted()

    fun onProgress(progress: Int, completeState: Boolean)

    fun onResourceReady(bitmap: Bitmap?)

    fun onLoadFailed()
}