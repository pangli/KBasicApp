package com.kcrason.appbasic.widget.recyclerview

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.kcrason.appbasic.R


open class LoadMoreRecyclerView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : RecyclerView(context, attrs, defStyleAttr) {

    private var mHeaderView: View? = null

    private var mLoadMoreFooterView: LoadMoreFooterView? = null

    private var mRecyclerLoadMoreListener: OnRecyclerLoadMoreListener? = null

    private var mRecyclerScrollStateListener: OnRecyclerScrollStateListener? = null
    private var isLoading = false
    private var isCanLoadMore = true


    fun addHeaderView(headerView: View?) {
        this.mHeaderView = headerView
    }

    fun setRecyclerLoadMoreListener(recyclerLoadMoreListener: OnRecyclerLoadMoreListener) {
        this.mRecyclerLoadMoreListener = recyclerLoadMoreListener
    }

    fun defaultShowLoading(onShowLoadingListener: (() -> Unit)? = null) {
        mLoadMoreFooterView?.showLoadingView()
        onShowLoadingListener?.invoke()
    }

    fun resetFooterView() {
        isLoading = false
        mLoadMoreFooterView?.hideFooterView()
    }

    fun canLoadMore(isCanLoadMore: Boolean) {
        this.isCanLoadMore = isCanLoadMore
    }

    fun loadErrorText() {
        isLoading = false
        mLoadMoreFooterView?.loadErrorText()
    }

    fun allLoadCompleteText(description: String = context.getString(R.string.not_more_data)) {
        isLoading = false
        isCanLoadMore = false
        mLoadMoreFooterView?.allLoadCompleteText(description)
    }

    fun allLoadCompleteGone() {
        isLoading = false
        isCanLoadMore = false
        mLoadMoreFooterView?.allLoadCompleteGone()
    }

    fun currentLoadComplete() {
        this.isLoading = false
    }


    override fun setAdapter(adapter: Adapter<ViewHolder>?) {
        val footerAdapter =
            LoadMoreRecyclerFooterAdapter(adapter ?: throw NullPointerException("Adapter is null"))
        footerAdapter.addHeaderView(mHeaderView)
        mLoadMoreFooterView = LoadMoreFooterView(context)
        footerAdapter.addFooterView(mLoadMoreFooterView!!)
        super.setAdapter(footerAdapter)
        adapter.registerAdapterDataObserver(KAdapterDataObserver(footerAdapter))
    }

    class KAdapterDataObserver(adapter: LoadMoreRecyclerFooterAdapter) :
        RecyclerView.AdapterDataObserver() {

        private var mAdapter: LoadMoreRecyclerFooterAdapter? = null

        init {
            this.mAdapter = adapter
        }

        override fun onChanged() {
            mAdapter?.notifyDataSetChanged()
        }

        override fun onItemRangeChanged(positionStart: Int, itemCount: Int) {
            mAdapter?.notifyItemRangeChanged(positionStart, itemCount)
        }

        override fun onItemRangeChanged(positionStart: Int, itemCount: Int, payload: Any?) {
            mAdapter?.notifyItemRangeChanged(positionStart, itemCount, payload)
        }

        override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
            mAdapter?.notifyItemRangeInserted(positionStart, itemCount)
        }

        override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) {
            mAdapter?.notifyItemRangeRemoved(positionStart, itemCount)
        }

        override fun onItemRangeMoved(fromPosition: Int, toPosition: Int, itemCount: Int) {
            mAdapter?.notifyItemMoved(fromPosition, toPosition)
        }
    }


    override fun onScrollStateChanged(state: Int) {
        super.onScrollStateChanged(state)
        if (state == SCROLL_STATE_IDLE) {
            mRecyclerScrollStateListener?.onRecyclerScrollState(false)
        } else {
            mRecyclerScrollStateListener?.onRecyclerScrollState(true)
        }
    }


    override fun onScrolled(dx: Int, dy: Int) {
        super.onScrolled(dx, dy)
        if (!isLoading && isCanLoadMore) {
            val lastVisiblePosition: Int = findLastPosition(layoutManager)
            if (layoutManager!!.childCount > (if (mHeaderView == null) 1 else 2)
                && lastVisiblePosition >= layoutManager!!.itemCount - 1
            ) {
                mLoadMoreFooterView?.showLoadingView()
                isLoading = true
                mRecyclerLoadMoreListener?.onRecyclerLoadMore()
            }
        }
    }

    private fun getLastPosition(array: IntArray): Int {
        var lastPosition = array[0]
        for (value in array) {
            if (value > lastPosition) {
                lastPosition = value
            }
        }
        return lastPosition
    }

    private fun findLastPosition(layoutManager: LayoutManager?): Int {
        var lastVisiblePosition = 0

        if (layoutManager is LinearLayoutManager) {
            lastVisiblePosition = layoutManager.findLastVisibleItemPosition()
        } else if (layoutManager is StaggeredGridLayoutManager) {
            val arrays = intArrayOf(layoutManager.spanCount)
            layoutManager.findLastVisibleItemPositions(arrays)
            lastVisiblePosition = getLastPosition(arrays)
        }
        return lastVisiblePosition
    }

}