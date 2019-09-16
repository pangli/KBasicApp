package com.kcrason.appbasic.widget.recyclerview

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.kcrason.appbasic.common.helper.LogHelper.info
import java.lang.NullPointerException

class LoadMoreRecyclerFooterAdapter(adapter: RecyclerView.Adapter<RecyclerView.ViewHolder>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var mAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder> = adapter

    val realAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>?
        get() = mAdapter

    private var mRecyclerViewHeaderView: View? = null

    private var mFooterView: View? = null


    fun addHeaderView(headerView: View?) {
        headerView?.layoutParams =
            RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        this.mRecyclerViewHeaderView = headerView
    }

    fun addFooterView(footerView: View) {
        this.mFooterView = footerView
    }

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, i: Int) {
        if (mRecyclerViewHeaderView == null) {
            if (!isFooter(i)) {
                mAdapter.onBindViewHolder(viewHolder, i)
            } else {
                info("running in footer：$i ")
            }
        } else {
            if (i == 0 && viewHolder is RecyclerViewHeaderViewHolder) {

            } else if (!isFooter(i)) {
                mAdapter.onBindViewHolder(viewHolder, i - 1)
            }
        }
    }


    override fun getItemCount(): Int {
        return if (mRecyclerViewHeaderView == null) {
            mAdapter.itemCount + VIEW_TYPE_COUNT_NO_HEADER
        } else {
            mAdapter.itemCount + VIEW_TYPE_COUNT_HAS_HEADER
        }
    }

    override fun getItemViewType(position: Int): Int {
        return (
                if (mRecyclerViewHeaderView == null) {
                    if (isFooter(position)) {
                        TYPE_FOOTER
                    } else {
                        mAdapter.getItemViewType(position)
                    }
                } else
                    if (position == 0) {
                        TYPE_HEAD
                    } else if (isFooter(position)) {
                        TYPE_FOOTER
                    } else {
                        mAdapter.getItemViewType(position - 1)
                    }
                )
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_HEAD -> if (mRecyclerViewHeaderView == null) throw NullPointerException("") else RecyclerViewHeaderViewHolder(
                mRecyclerViewHeaderView!!
            )
            TYPE_FOOTER -> if (mFooterView == null) throw NullPointerException("") else RecyclerViewFooterViewHolder(
                mFooterView!!
            )
            else -> mAdapter.onCreateViewHolder(parent, viewType)
        }
    }


    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        val layoutManager: RecyclerView.LayoutManager? = recyclerView.layoutManager
        if (layoutManager is GridLayoutManager) layoutManager.spanSizeLookup =
            object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(p0: Int): Int {
                    return if ((getItemViewType(p0) == TYPE_HEAD || getItemViewType(p0) == TYPE_FOOTER)) layoutManager.spanCount else 1
                }
            }
    }


    private fun isFooter(position: Int): Boolean {
        return position < itemCount && position >= itemCount - 1
    }

    override fun onViewAttachedToWindow(holder: RecyclerView.ViewHolder) {
        super.onViewAttachedToWindow(holder)
        val lp = holder.itemView.layoutParams
        if (lp is StaggeredGridLayoutManager.LayoutParams
            && getItemViewType(holder.layoutPosition) == TYPE_HEAD
            && isFooter(holder.layoutPosition)
        ) {
            lp.isFullSpan = true
        }
    }

    class RecyclerViewHeaderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    class RecyclerViewFooterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    companion object {
        private const val TYPE_HEAD: Int = -1
        private const val TYPE_FOOTER: Int = -2

        /**
         * 不包含头部，默认添加了底部，则默认itemcount+1
         */
        private const val VIEW_TYPE_COUNT_NO_HEADER = 1

        /**
         * 包含头部，默认添加了底部，则默认itemcount+2
         */
        private const val VIEW_TYPE_COUNT_HAS_HEADER = 2
    }
}