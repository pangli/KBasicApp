package com.kcrason.appbasic.network.rxjava

import android.content.Context
import com.kcrason.appbasic.network.exception.ApiException
import com.kcrason.appbasic.common.addDisposable
import com.kcrason.appbasic.mvp.model.base.BaseArrayObj
import com.kcrason.appbasic.mvp.model.base.BaseModel
import com.kcrason.appbasic.mvp.model.base.BaseObj
import io.reactivex.SingleObserver
import io.reactivex.disposables.Disposable

abstract class BasicSingleObserver<T>(context: Context?) : SingleObserver<T> {

    var mContext: Context? = context

    override fun onSuccess(t: T) {
        if (t is BaseObj<*>) {
            if (t.isOk()) {
                onResult(t)
            } else {
                onFailure(ApiException(t.code, t.msg))
            }
        } else if (t is BaseArrayObj<*>) {
            if (t.isOk()) {
                onResult(t)
            } else {
                onFailure(ApiException(t.code, t.msg))
            }
        } else if (t is BaseModel) {
            if (t.isOk()) {
                onResult(t)
            } else {
                onFailure(ApiException(t.code, t.msg))
            }
        } else {
            //自行处理异常相关操作
            onResult(t)
        }
    }


    override fun onError(e: Throwable) {
        if (e is ApiException) {
            onFailure(e)
        } else {
            onFailure(ApiException(-1, e.message ?: "unknown error message"))
        }
    }

    abstract fun onResult(result: T)

    abstract fun onFailure(apiException: ApiException)


    override fun onSubscribe(d: Disposable) {
        mContext.addDisposable(d)
    }
}