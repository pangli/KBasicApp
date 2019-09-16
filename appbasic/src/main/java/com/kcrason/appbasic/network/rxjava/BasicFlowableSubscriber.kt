package com.kcrason.appbasic.network.rxjava

import android.content.Context
import com.kcrason.appbasic.common.addSubscription
import io.reactivex.FlowableSubscriber
import org.reactivestreams.Subscription

/**
 * @author KCrason
 * @date 2019/6/14 17:59
 * @description
 */
abstract class BasicFlowableSubscriber<T>(context: Context?) : FlowableSubscriber<T> {

    private var mContext = context

    override fun onComplete() {
    }


    override fun onSubscribe(s: Subscription) {
        s.request(Long.MAX_VALUE)
        mContext.addSubscription(s)
    }
}