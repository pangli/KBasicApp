package com.kcrason.appbasic.widget.listview

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.AbsListView
import android.widget.ListView
import com.kcrason.appbasic.R
import com.kcrason.appbasic.common.helper.NetworkHelper
import kotlinx.android.synthetic.main.view_list_load_more_footer.view.*

class LoadMoreListView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ListView(context, attrs, defStyleAttr), AbsListView.OnScrollListener {


    private var isCanLoadMore = true
    private var isLoading = false

    private var mLastVisibleItem = 0
    private var mTotalItemCount = 0

    private var mOutScrollListener: OnOutScrollListener? = null

    private var mOnListViewLoadMoreListener: OnListViewLoadMoreListener? = null


    fun setOnOutScrollListener(outScrollListener: OnOutScrollListener) {
        this.mOutScrollListener = outScrollListener
    }

    fun setListViewLoadMoreListener(listViewLoadMoreListener: OnListViewLoadMoreListener) {
        this.mOnListViewLoadMoreListener = listViewLoadMoreListener
    }


    init {
        val footerView: View =
            LayoutInflater.from(context).inflate(R.layout.view_list_load_more_footer, this, false)
        footerView.isEnabled = false
        addFooterView(footerView)
        setOnScrollListener(this)
    }


    fun setCanLoadMore(isCanLoadMore: Boolean) {
        this.isCanLoadMore = isCanLoadMore
    }


    override fun onScrollStateChanged(view: AbsListView?, scrollState: Int) {
        mOutScrollListener?.onScrollStateChanged(view, scrollState)
        if (mTotalItemCount == mLastVisibleItem && scrollState == OnScrollListener.SCROLL_STATE_IDLE && !isLoading && isCanLoadMore) {
            if (mOnListViewLoadMoreListener != null) {
                isLoading = true
                mOnListViewLoadMoreListener?.onListViewLoadMore()
            }
        }
    }

    override fun onScroll(
        view: AbsListView?,
        firstVisibleItem: Int,
        visibleItemCount: Int,
        totalItemCount: Int
    ) {
        this.mLastVisibleItem = firstVisibleItem + visibleItemCount
        this.mTotalItemCount = totalItemCount
        mOutScrollListener?.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount)
    }


    fun loadErrorText() {
        isLoading = false
        footer_loading.visibility = View.GONE
        txt_message.visibility = View.GONE
        txt_message.text =
            if (NetworkHelper.isNetWorkAvailable(context)) "数据加载失败" else "暂无网络，请检查网设置"
    }

    fun allLoadCompleteText() {
        isLoading = false
        footer_loading.visibility = View.GONE
        txt_message.visibility = View.VISIBLE
        txt_message.text = "没有更多数据了"
    }

    fun currentLoadComplete() {
        isLoading = false
    }
}