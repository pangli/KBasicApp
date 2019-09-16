package com.kcrason.appbasic.common.helper

import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import org.reactivestreams.Subscription


/**
 * @author KCrason
 * @date 2019/6/14 18:03
 * @description RxJava请求管理类
 */
class CompositeDisposableHelper {

    private val mCompositeDisposable = CompositeDisposable()

    private val mSubscriptionArrayData = ArrayList<Subscription>()

    fun addDisposable(disposable: Disposable) {
        mCompositeDisposable.add(disposable)
    }

    fun addSubscription(subscription: Subscription) {
        mSubscriptionArrayData.add(subscription)
    }

    fun clearRequest() {
        clearDisposable()
        clearSubscription()
    }

    private fun clearDisposable() {
        mCompositeDisposable.clear()
    }

    private fun clearSubscription() {
        mSubscriptionArrayData.forEach {
            it.cancel()
        }
    }
}