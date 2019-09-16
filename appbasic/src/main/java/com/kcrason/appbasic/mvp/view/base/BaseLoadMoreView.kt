package com.kcrason.appbasic.mvp.view.base

import com.kcrason.appbasic.mvp.model.base.BaseArrayObj

/**
 * @author KCrason
 * @date 2019/5/23 14:08
 * @description
 */
interface BaseLoadMoreView<D> : BaseView {
    fun onDataResult(result: BaseArrayObj<D>)

    fun onDataFailure()
}