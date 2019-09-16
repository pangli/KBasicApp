package com.kcrason.appbasic.common

import java.math.RoundingMode
import java.text.NumberFormat

/**
 * @author KCrason
 * @date 2019/6/26 16:28
 * @description
 */
/**
 * Double扩展函数：保留两位小数，结果并返回String
 */
fun Double?.doubleKeepTwoDecimals(): String {
    return if (this == null) {
        "--"
    } else {
        String.format("%.2f", this)
    }
}

/**
 * Double扩展函数：将double转成百分比并保留两位小数，结果返回String
 */
fun Double?.doubleToPercentKeepTwoDecimals(): String {
    return if (this == null) {
        "--"
    } else {
        val numberFormat = NumberFormat.getPercentInstance()
        numberFormat.minimumFractionDigits = 2//设置保留小数位
        numberFormat.roundingMode = RoundingMode.HALF_UP //设置舍入模式
        numberFormat.format(this)
    }
}
