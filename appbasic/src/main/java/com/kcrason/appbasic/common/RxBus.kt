package com.kcrason.appbasic.common

import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

/**
 * @author KCrason
 * @date 2019/7/3 16:06
 * @description
 */
object RxBus {

    private val publisher = PublishSubject.create<Any>()

    fun publish(event: Any) {
        publisher.onNext(event)
    }

    fun <T> listen(eventType: Class<T>): Observable<T> = publisher.ofType(eventType)
}