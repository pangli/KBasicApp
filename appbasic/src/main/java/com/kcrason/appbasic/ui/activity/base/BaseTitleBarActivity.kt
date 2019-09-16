package com.fid.basic.ui.activity.base

import android.view.LayoutInflater
import android.view.View
import com.kcrason.appbasic.mvp.presenter.base.BasePresenter
import com.kcrason.appbasic.mvp.view.base.BaseView
import com.kcrason.appbasic.widget.layouts.CommonTitleBar
import com.kcrason.appbasic.widget.layouts.MultiStateView
import com.kcrason.appbasic.R
import com.kcrason.appbasic.ui.activity.base.BaseActivity
import kotlinx.android.synthetic.main.activity_base_title_bar.*
import kotlinx.android.synthetic.main.activity_base_title_bar.commonTitleBar

abstract class BaseTitleBarActivity<T : BasePresenter<V>, V : BaseView> : BaseActivity<T, V>() {

    final override fun getLayoutId(): Int = R.layout.activity_base_title_bar

    final override fun init() {
        commonTitleBar.setCenterText(getTopTitle())
        commonTitleBar.setLeftIconVisible(if (showBack()) View.VISIBLE else View.GONE)
        LayoutInflater.from(this).inflate(getContainerLayout(), multiStateView, true)
        initialize()
    }

    fun getCommonTitleBar(): CommonTitleBar {
        return commonTitleBar
    }

    fun getMultiStateView(): MultiStateView {
        return multiStateView
    }

    open fun showBack(): Boolean {
        return true
    }

    abstract fun initialize()

    abstract fun getContainerLayout(): Int

    abstract fun getTopTitle(): String?
}