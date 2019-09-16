package com.kcrason.appbasic.network.rxjava

import android.content.Context
import com.kcrason.appbasic.common.helper.LogHelper


/**
 * @author KCrason
 * @date 2019/6/14 17:59
 * @description
 */
abstract class BasicFlowableSubscriberSuccess<T>(context: Context?) : BasicFlowableSubscriber<T>(context) {
    override fun onError(t: Throwable?) {
        LogHelper.info(t?.message ?: "default error message")
    }
}