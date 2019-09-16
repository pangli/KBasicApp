package com.kcrason.appbasic.ui.fragment.base

import android.view.LayoutInflater
import android.view.View
import com.kcrason.appbasic.mvp.presenter.base.BasePresenter
import com.kcrason.appbasic.mvp.view.base.BaseView
import com.kcrason.appbasic.widget.layouts.CommonTitleBar
import com.kcrason.appbasic.widget.layouts.MultiStateView
import com.kcrason.appbasic.R
import kotlinx.android.synthetic.main.activity_base_title_bar.*

abstract class BaseTitleBarFragment<T : BasePresenter<V>, V : BaseView> : BaseFragment<T, V>() {

    final override fun getLayoutId(): Int {
        return R.layout.fragment_base_title_bar
    }

    fun getCommonTitleBar(): CommonTitleBar {
        return commonTitleBar
    }

    fun getMultiStateView(): MultiStateView {
        return multiStateView
    }

    final override fun init() {
        commonTitleBar.setCenterText(getTopTitle())
        commonTitleBar.setLeftIconVisible(if (showBack()) View.VISIBLE else View.GONE)
        LayoutInflater.from(context).inflate(getContainerLayout(), multiStateView, true)
        initialize()
    }

    open fun showBack(): Boolean {
        return true
    }

    abstract fun initialize()

    abstract fun getContainerLayout(): Int

    abstract fun getTopTitle(): String
}