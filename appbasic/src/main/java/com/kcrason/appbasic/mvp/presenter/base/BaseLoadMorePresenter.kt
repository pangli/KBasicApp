package com.kcrason.appbasic.mvp.presenter.base

import android.content.Context
import com.kcrason.appbasic.mvp.view.base.BaseLoadMoreView

/**
 * @author KCrason
 * @date 2019/5/23 14:08
 * @description
 */
open class BaseLoadMorePresenter<V : BaseLoadMoreView<D>, D>(context: Context?, baseView: V) :
    BasePresenter<V>(context, baseView)