package com.kcrason.appbasic.adapter.base

import android.content.Context
import android.view.ViewGroup

abstract class

SimpleRecyclerAdapter<T>(context: Context, itemLayoutId: Int) : BaseRecyclerAdapter<T>(context) {

    private var mItemLayoutId = 0

    init {
        mItemLayoutId = itemLayoutId
    }

    override fun getItemLayoutId(parent: ViewGroup, viewType: Int): Int {
        if (mItemLayoutId == 0) {
            throw IllegalArgumentException("layout may be is null! please set a layout resource.")
        }
        return mItemLayoutId
    }
}