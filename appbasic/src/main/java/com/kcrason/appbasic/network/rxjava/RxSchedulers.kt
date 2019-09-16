package com.kcrason.appbasic.network.rxjava

import io.reactivex.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * @author KCrason
 * @date 2019/5/21 17:34
 * @description
 */
object RxSchedulers {


    /**
     * Single从io转到主线程
     *
     * @param <T>
     * @return
    </T> */
    fun <T> applySingleSchedulers(): SingleTransformer<T, T> {
        return SingleTransformer { upstream -> upstream.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()) }
    }


    /**
     * Flowable从io转到主线程
     */
    fun <T> applyFlowableSchedulers(): FlowableTransformer<T, T> {
        return FlowableTransformer { upstream -> upstream.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()) }
    }

}