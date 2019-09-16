package com.kcrason.appbasic.network.rxjava

import android.content.Context
import com.kcrason.appbasic.network.exception.ApiException
import com.kcrason.appbasic.common.helper.LogHelper

abstract class BasicSingleObserverSuccess<T>(context: Context?) : BasicSingleObserver<T>(context) {
    override fun onFailure(apiException: ApiException) {
        LogHelper.info(apiException.message ?: "unknown error message")
    }
}