package com.kcrason.appbasic.ui.fragment.base

import android.os.Bundle
import android.view.View
import com.kcrason.appbasic.mvp.presenter.base.BasePresenter
import com.kcrason.appbasic.mvp.view.base.BaseView

abstract class BaseLazyFragment<T : BasePresenter<V>, V : BaseView> : BaseFragment<T, V>() {

    private var isLoadComplete = false
    private var isViewPrepare = false
    private var isCurFragmentVisible = false

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        this.isCurFragmentVisible = isVisibleToUser
        if (isCurFragmentVisible) {
            localStartWork()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        isViewPrepare = true
        localStartWork()
    }

    private fun localStartWork() {
        if (isCurFragmentVisible && isViewPrepare && !isLoadComplete) {
            onLazyWork()
            isLoadComplete = true
        }
    }

    protected abstract fun onLazyWork()
}