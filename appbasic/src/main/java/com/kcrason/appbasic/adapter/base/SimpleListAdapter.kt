package com.kcrason.appbasic.adapter.base

import android.content.Context

abstract class SimpleListAdapter<T>(context: Context, itemLayoutId: Int) :
    BaseListAdapter<T>(context) {

    private var mItemLayoutId: Int = 0

    init {
        this.mItemLayoutId = itemLayoutId
    }

    override fun getItemLayoutId(): Int {
        if (mItemLayoutId == 0) {
            throw IllegalArgumentException("layout may be is null! please set a layout resource.")
        }
        return mItemLayoutId
    }
}