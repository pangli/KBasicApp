package com.kcrason.appbasic.widget.recyclerview

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.kcrason.appbasic.R


class TopLoadMoreRecyclerView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : RecyclerView(context, attrs, defStyleAttr) {

    private var mHeaderView: View? = null

    private var mLoadMoreHeaderView: LoadMoreHeaderView? = null

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

    fun resetHeadererView() {
        isLoading = false
        mLoadMoreHeaderView?.hideFooterView()
    }

    fun canLoadMore(isCanLoadMore: Boolean) {
        this.isCanLoadMore = isCanLoadMore
    }

    fun loadErrorText() {
        isLoading = false
        mLoadMoreHeaderView?.loadErrorText()
    }

    fun allLoadCompleteText(description: String = context.getString(R.string.not_more_data)) {
        isLoading = false
        isCanLoadMore = false
        mLoadMoreHeaderView?.allLoadCompleteText(description)
    }

    fun allLoadCompleteGone() {
        isLoading = false
        isCanLoadMore = false
        mLoadMoreHeaderView?.allLoadCompleteGone()
    }

    fun currentLoadComplete() {
        this.isLoading = false
    }

    override fun setAdapter(adapter: Adapter<ViewHolder>?) {
        val footerAdapter =
            LoadMoreRecyclerHeaderAdapter(adapter ?: throw NullPointerException("Adapter is null"))
        footerAdapter.addHeaderView(mHeaderView)
        mLoadMoreHeaderView = LoadMoreHeaderView(context)
        footerAdapter.addHeaderView(mLoadMoreHeaderView!!)
        super.setAdapter(footerAdapter)
        adapter.registerAdapterDataObserver(KAdapterDataObserver(footerAdapter))
    }

    class KAdapterDataObserver(adapter: LoadMoreRecyclerHeaderAdapter) : RecyclerView.AdapterDataObserver() {

        private var mAdapter: LoadMoreRecyclerHeaderAdapter? = null

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
            val firstVisiblePosition: Int = findFirstPosition(layoutManager)
            if (layoutManager!!.childCount > 1
                && firstVisiblePosition == 0
            ) {
                mLoadMoreHeaderView?.showLoadingView()
                isLoading = true
                mRecyclerLoadMoreListener?.onRecyclerLoadMore()
            }
        }
    }

    private fun getFirstPosition(array: IntArray): Int {
        var firstPosition = array[0]
        for (value in array) {
            if (value < firstPosition) {
                firstPosition = value
            }
        }
        return firstPosition
    }

    private fun findFirstPosition(layoutManager: LayoutManager?): Int {
        var firstVisiblePosition = 0

        if (layoutManager is LinearLayoutManager) {
            firstVisiblePosition = layoutManager.findFirstVisibleItemPosition()
        } else if (layoutManager is StaggeredGridLayoutManager) {
            val arrays = intArrayOf(layoutManager.spanCount)
            layoutManager.findFirstVisibleItemPositions(arrays)
            firstVisiblePosition = getFirstPosition(arrays)
        }
        return firstVisiblePosition
    }

}