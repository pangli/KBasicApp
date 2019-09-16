package com.kcrason.appbasic.mvp.presenter.base

import android.content.Context
import com.kcrason.appbasic.mvp.view.base.BaseView

open class BasePresenter<V : BaseView>(context: Context?, baseView: V?) {

    private var mBaseView: V? = baseView

    private var mContext: Context? = context

    val context: Context? get() = mContext

    val view: V? get() = mBaseView

    fun clearBaseView() {
        mBaseView = null
    }
}
