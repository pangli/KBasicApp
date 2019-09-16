package com.kcrason.appbasic.widget.recyclerview

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import java.lang.NullPointerException

class LoadMoreRecyclerHeaderAdapter(adapter: RecyclerView.Adapter<RecyclerView.ViewHolder>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var mAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder> = adapter

    val realAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>?
        get() = mAdapter

    private var mRecyclerViewHeaderView: View? = null


    fun addHeaderView(headerView: View?) {
        headerView?.layoutParams =
            RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        this.mRecyclerViewHeaderView = headerView
    }


    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, i: Int) {
        if (i == 0 && viewHolder is RecyclerViewHeaderViewHolder) {

        } else {
            mAdapter.onBindViewHolder(viewHolder, i - 1)
        }
    }


    override fun getItemCount(): Int {
        return mAdapter.itemCount + VIEW_TYPE_COUNT_HEADER
    }

    override fun getItemViewType(position: Int): Int {
        return (if (position == 0) {
            TYPE_HEAD
        } else {
            mAdapter.getItemViewType(position - 1)
        })
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_HEAD -> if (mRecyclerViewHeaderView == null) throw NullPointerException("") else RecyclerViewHeaderViewHolder(
                mRecyclerViewHeaderView!!
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
                    return if ((getItemViewType(p0) == TYPE_HEAD)) layoutManager.spanCount else 1
                }
            }
    }



    override fun onViewAttachedToWindow(holder: RecyclerView.ViewHolder) {
        super.onViewAttachedToWindow(holder)
        val lp = holder.itemView.layoutParams
        if (lp is StaggeredGridLayoutManager.LayoutParams
            && getItemViewType(holder.layoutPosition) == TYPE_HEAD
        ) {
            lp.isFullSpan = true
        }
    }

    class RecyclerViewHeaderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    companion object {
        private const val TYPE_HEAD: Int = -1
        private const val VIEW_TYPE_COUNT_HEADER = 1
    }
}