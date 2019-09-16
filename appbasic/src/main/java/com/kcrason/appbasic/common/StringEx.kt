package com.kcrason.appbasic.common

import android.text.Html
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.text.style.URLSpan
import android.text.util.Linkify
import java.lang.NumberFormatException
import java.util.regex.Pattern

/**
 * @author KCrason
 * @date 2019/6/26 16:27
 * @description
 */

/**
 * String扩展函数：格式化String中的html标签
 */
fun String?.formatHtmlTag(): String {
    @Suppress("DEPRECATION")
    return Html.fromHtml(this ?: "").toString()
}

/**
 * String扩展函数，将总市值数据格式化为double，并处理空值，方便进行排序
 */
fun String?.totalPriceToDouble(): Double {
    return if (this.isNullOrEmpty()) {
        0.00
    } else {
        try {
            this.substring(0, length - 1).toDouble()
        } catch (e: Exception) {
            0.00
        }
    }
}

/**
 * String扩展函数，将String格式化为double，并处理空值
 */
fun String?.formatToDouble(): Double {
    return if (this.isNullOrEmpty()) {
        0.00
    } else {
        try {
            this.toDouble()
        } catch (e: NumberFormatException) {
            0.00
        }
    }
}


/**
 * String扩展函数：将指定字符串中对应匹配的关键字格式化需要的颜色。支持替换关键字为指定关键字
 */
fun String?.generatePatternForegroundColorSpan(
    matchKeyword: String,
    replaceKeyword: String,
    targetColor: Int
): SpannableStringBuilder {
    if (this.isNullOrEmpty()) {
        return SpannableStringBuilder(this)
    }
    val spannableStringBuilder = SpannableStringBuilder(this)
    Linkify.addLinks(
        spannableStringBuilder,
        Pattern.compile(matchKeyword, Pattern.CASE_INSENSITIVE),
        ""
    )
    val urlSpans =
        spannableStringBuilder.getSpans(0, spannableStringBuilder.length, URLSpan::class.java)
    for (urlSpan in urlSpans) {
        val start = spannableStringBuilder.getSpanStart(urlSpan)
        val end = spannableStringBuilder.getSpanEnd(urlSpan)
        spannableStringBuilder.removeSpan(urlSpan)
        if (start >= 0 && end > 0 && start < end) {
            spannableStringBuilder.setSpan(
                ForegroundColorSpan(targetColor),
                start,
                end,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            if (matchKeyword != replaceKeyword) {
                spannableStringBuilder.replace(start, end, replaceKeyword)
            }
        }
    }
    return spannableStringBuilder
}


