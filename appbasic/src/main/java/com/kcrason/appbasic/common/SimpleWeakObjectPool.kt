package com.kcrason.appbasic.common

import java.lang.ref.WeakReference

/**
 * @author KCrason
 * @date 2018/11/30
 */
class SimpleWeakObjectPool<T> @JvmOverloads constructor(size: Int = 5) {

    private var objsPool: Array<WeakReference<T>?>? = arrayOfNulls(size)
    private var curPointer = -1

    @Synchronized
    fun get(): T? {
        if (curPointer == -1 || curPointer > objsPool!!.size) return null
        val obj = objsPool!![curPointer]?.get()
        objsPool!![curPointer] = null
        curPointer--
        return obj
    }

    @Synchronized
    fun put(t: T): Boolean {
        if (curPointer == -1 || curPointer < objsPool!!.size - 1) {
            curPointer++
            objsPool?.set(curPointer, WeakReference(t))
            return true
        }
        return false
    }

    fun clearPool() {
        for (i in objsPool!!.indices) {
            objsPool!![i]?.clear()
            objsPool!![i] = null
        }
        curPointer = -1
    }

    fun size(): Int {
        return if (objsPool == null) 0 else objsPool!!.size
    }
}
