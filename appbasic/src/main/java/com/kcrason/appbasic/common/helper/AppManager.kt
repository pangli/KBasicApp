package com.kcrason.appbasic.common.helper

import android.app.Activity
import java.util.*

/**
 * @author KCrason
 * @date 2019/5/31 16:18
 * @description
 */
object AppManager {

    private var mActivities: Stack<Activity> = Stack()


    /**
     * 添加Activity到堆栈
     */
    fun attach(activity: Activity) {
        mActivities.add(activity)
    }

    /**
     * 将指定Activity移除
     */
    fun detach(activity: Activity) {
        mActivities.remove(activity)
    }

    /**
     * Get current activity (the last into Stack)
     */
    fun currentActivity(): Activity {
        return mActivities.lastElement()
    }


    fun isExistActivity(className: String): Boolean {
        for (index in 0 until mActivities.size) {
            if (mActivities[index].javaClass.name == className) {
                return true
            }
        }
        return false
    }

    /**
     * Finish all activity of th mActivities and make mActivities clear
     */
    fun finishAllActivity() {
        mActivities.forEach {
            it?.finish()
        }
        mActivities.clear()
    }
}