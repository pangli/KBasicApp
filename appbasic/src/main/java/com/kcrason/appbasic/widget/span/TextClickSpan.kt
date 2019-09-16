package com.kcrason.appbasic.widget.span

import android.text.TextPaint
import android.text.style.ClickableSpan
import android.view.View

/**
 * @author KCrason
 * @date 2019/7/4 10:50
 * @description
 */
class TextClickSpan(textColor: Int, text: String, onClickListener: ((text: String) -> Unit)?) : ClickableSpan() {

    private val mTextColor = textColor
    private val mText = text
    private val mOnClickListener = onClickListener

    override fun updateDrawState(ds: TextPaint) {
        super.updateDrawState(ds)
        ds.isUnderlineText = false
        ds.color = mTextColor
    }

    override fun onClick(widget: View) {
        mOnClickListener?.invoke(mText)
    }
}