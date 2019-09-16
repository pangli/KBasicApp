package com.kcrason.appbasic.adapter.base

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kcrason.appbasic.adapter.viewholder.BaseRecyclerViewHolder
import kotlin.collections.ArrayList

abstract class BaseRecyclerAdapter<T>(context: Context) :
    RecyclerView.Adapter<BaseRecyclerViewHolder>() {

    val arrayData: ArrayList<T> = ArrayList()

    val mContext: Context = context

    /**
     * 检查当前索引是否在指定数组范围
     */
    fun inRangArray(index: Int): Boolean {
        return (index >= 0 && index < arrayData.size)
    }

    fun clearRecyclerView() {
        arrayData.clear()
        notifyDataSetChanged()
    }


    fun showLoadMoreData(newArrayData: List<T>, isReverse: Boolean = false) {
        arrayData.addAll(if (isReverse) newArrayData.asReversed() else newArrayData)
        notifyItemRangeChanged(arrayData.size, newArrayData.size)
    }

    fun showRefreshData(newArrayData: List<T>) {
        arrayData.clear()
        arrayData.addAll(newArrayData)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return arrayData.size
    }

    protected abstract fun getItemLayoutId(parent: ViewGroup, viewType: Int): Int

    protected abstract fun bindData(
        baseRecyclerViewHolder: BaseRecyclerViewHolder,
        data: T?,
        position: Int
    )

    final override fun onCreateViewHolder(p0: ViewGroup, p1: Int): BaseRecyclerViewHolder {
        val itemView: View =
            LayoutInflater.from(p0.context).inflate(getItemLayoutId(p0, p1), p0, false)
        return BaseRecyclerViewHolder(itemView)
    }

    final override fun onBindViewHolder(
        holder: BaseRecyclerViewHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {
        super.onBindViewHolder(holder, position, payloads)
    }

    final override fun onBindViewHolder(holder: BaseRecyclerViewHolder, position: Int) {
        bindData(holder, if (inRangArray(position)) arrayData[position] else null, position)
    }
}