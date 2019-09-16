package com.kcrason.appbasic.ui.activity.base

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.bumptech.glide.Glide
import com.fid.basic.ui.activity.base.BaseTitleBarActivity
import com.kcrason.appbasic.adapter.base.SimpleRecyclerAdapter
import com.kcrason.appbasic.common.autoRefresh
import com.kcrason.appbasic.common.closeRefresh
import com.kcrason.appbasic.common.helper.NetworkHelper
import com.kcrason.appbasic.mvp.model.base.BaseArrayObj
import com.kcrason.appbasic.mvp.presenter.base.BaseLoadMorePresenter
import com.kcrason.appbasic.mvp.view.base.BaseLoadMoreView
import com.kcrason.appbasic.widget.layouts.ASwipeRefreshLayout
import com.kcrason.appbasic.widget.recyclerview.LoadMoreRecyclerView
import com.kcrason.appbasic.widget.recyclerview.OnRecyclerLoadMoreListener
import com.kcrason.appbasic.R
import kotlinx.android.synthetic.main.activity_base_load_more.*
import kotlinx.android.synthetic.main.activity_base_title_bar.*

/**
 * @author KCrason
 * @date 2019/5/23 13:57
 * @description 封装一层纯recycleview支持加载更多的列表
 */
abstract class BaseLoadMoreActivity<P : BaseLoadMorePresenter<V, D>, V : BaseLoadMoreView<D>, D> :
    BaseTitleBarActivity<P, V>(), BaseLoadMoreView<D>, SwipeRefreshLayout.OnRefreshListener,
    OnRecyclerLoadMoreListener {

    private var mLoadMore: Boolean = false

    private var mPageNum: Int = 1

    val pageNum: Int get() = mPageNum

    private var mSimpleRecyclerAdapter: SimpleRecyclerAdapter<D>? = null

    private val loadMore: Boolean
        get() = mLoadMore

    fun getLoadMoreRecyclerView(): LoadMoreRecyclerView {
        return loadMoreRecyclerView
    }

    fun getASwipeRefreshLayout(): ASwipeRefreshLayout {
        return swipeRefreshLayout
    }


    override fun getContainerLayout(): Int {
        return R.layout.activity_base_load_more
    }

    override fun initialize() {
        swipeRefreshLayout.setOnRefreshListener(this)
        loadMoreRecyclerView.setRecyclerLoadMoreListener(this)
        loadMoreRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (!isFinishing && controlGlideState()) {
                    if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                        Glide.with(this@BaseLoadMoreActivity).resumeRequests()
                    } else {
                        Glide.with(this@BaseLoadMoreActivity).pauseRequests()
                    }
                }
            }
        })
        loadMoreRecyclerView.layoutManager = LinearLayoutManager(this)
        mSimpleRecyclerAdapter = createSimpleRecyclerAdapter().apply { loadMoreRecyclerView.adapter = this }
        swipeRefreshLayout.autoRefresh {
            if (NetworkHelper.isNetWorkAvailable(this)) {
                refreshData()
            } else {
                swipeRefreshLayout.closeRefresh()
                multiStateView.loadFailure { refreshData() }
            }
        }
    }

    open fun controlGlideState(): Boolean = false

    abstract fun refreshData()

    abstract fun loadMoreData()

    abstract fun createSimpleRecyclerAdapter(): SimpleRecyclerAdapter<D>

    override fun onRecyclerLoadMore() {
        if (NetworkHelper.isNetWorkAvailable(this)) {
            mLoadMore = true
            mPageNum++
            loadMoreData()
        } else {
            loadMoreRecyclerView.loadErrorText()
        }
    }

    override fun onDataResult(result: BaseArrayObj<D>) {
        swipeRefreshLayout.closeRefresh()
        if (result.data == null || result.data?.isEmpty() == true) {
            if (loadMore) {
                loadMoreRecyclerView.allLoadCompleteText()
            } else {
                multiStateView.notData()
            }
        } else {
            if (loadMore) {
                mSimpleRecyclerAdapter?.showLoadMoreData(result.data!!)
                loadMoreRecyclerView.currentLoadComplete()
            } else {
                multiStateView.showContent()
                mSimpleRecyclerAdapter?.showRefreshData(result.data!!)
            }
        }
    }

    override fun onDataFailure() {
        if (loadMore) {
            mPageNum--
            loadMoreRecyclerView.loadErrorText()
        } else {
            multiStateView.loadFailure {
                refreshData()
            }
        }
    }

    override fun onRefresh() {
        if (NetworkHelper.isNetWorkAvailable(this)) {
            mLoadMore = false
            mPageNum = 1
            loadMoreRecyclerView.canLoadMore(true)
            refreshData()
        } else {
            swipeRefreshLayout.closeRefresh()
        }
    }
}