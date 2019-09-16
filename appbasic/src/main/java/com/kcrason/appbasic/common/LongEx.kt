package com.kcrason.appbasic.common

import java.text.SimpleDateFormat
import java.util.*

/**
 * @author KCrason
 * @date 2019/6/27 9:40
 * @description Long扩展函数
 */

/**
 *根据时间戳同当前时间进行对比：
 * 同一天：HH:mm
 * 同年不同天：MM-dd
 * 不同年：yyyy-MM-dd
 */
fun Long.formatTimeOfYearOrDay(): String {
    val now = Calendar.getInstance()
    val curYear = now.get(Calendar.YEAR)
    val calendar = Calendar.getInstance()
    calendar.timeInMillis = this
    val targetYear = calendar.get(Calendar.YEAR)
    return if (curYear == targetYear) {
        //同一年
        val curDay = now.get(Calendar.DATE)
        calendar.timeInMillis = this
        val targetDay = calendar.get(Calendar.DATE)
        if (curDay == targetDay) {
            //同一天
            SimpleDateFormat("HH:mm", Locale.CHINA).format(Date(this))
        } else {
            SimpleDateFormat("MM-dd HH:mm", Locale.CHINA).format(Date(this))
        }
    } else {
        SimpleDateFormat("yyyy-MM-dd", Locale.CHINA).format(Date(this))
    }
}

fun Long.formatChinaTimeOfYearOrDay2(): String {
    val now = Calendar.getInstance()
    val curYear = now.get(Calendar.YEAR)
    val calendar = Calendar.getInstance()
    calendar.timeInMillis = this
    val targetYear = calendar.get(Calendar.YEAR)
    return if (curYear == targetYear) {
        //同一年
        val curDay = now.get(Calendar.DATE)
        calendar.timeInMillis = this
        val targetDay = calendar.get(Calendar.DATE)
        if (curDay == targetDay) {
            //同一天
            SimpleDateFormat("HH:mm", Locale.CHINA).format(Date(this))
        } else {
            if (targetDay == curDay - 1) {
                "昨天 ${SimpleDateFormat("HH:mm", Locale.CHINA).format(Date(this))}"
            } else {
                SimpleDateFormat("M月d日", Locale.CHINA).format(Date(this))
            }
        }
    } else {
        SimpleDateFormat("yyyy年M月d日", Locale.CHINA).format(Date(this))
    }
}

fun Long.formatChinaTimeOfYearOrDay(): String {
    val now = Calendar.getInstance()
    val curYear = now.get(Calendar.YEAR)
    val calendar = Calendar.getInstance()
    calendar.timeInMillis = this
    val targetYear = calendar.get(Calendar.YEAR)
    return if (curYear == targetYear) {
        //同一年
        val curDay = now.get(Calendar.DATE)
        calendar.timeInMillis = this
        val targetDay = calendar.get(Calendar.DATE)
        if (curDay == targetDay) {
            //同一天
            SimpleDateFormat("HH:mm", Locale.CHINA).format(Date(this))
        } else {
            if (targetDay == curDay - 1) {
                "昨天 ${SimpleDateFormat("aaHH:mm", Locale.CHINA).format(Date(this))}"
            } else {
                SimpleDateFormat("M月d日 aaHH:mm", Locale.CHINA).format(Date(this))
            }
        }
    } else {
        SimpleDateFormat("yyyy年M月d日 aaHH:mm", Locale.CHINA).format(Date(this))
    }
}