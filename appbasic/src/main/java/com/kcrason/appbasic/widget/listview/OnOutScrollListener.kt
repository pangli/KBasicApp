package com.kcrason.appbasic.widget.listview

import android.widget.AbsListView

interface OnOutScrollListener {

    fun onScrollStateChanged(view: AbsListView?, scrollState: Int)

    fun onScroll(
        view: AbsListView?, firstVisibleItem: Int,
        visibleItemCount: Int, totalItemCount: Int
    )
}