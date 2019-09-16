package com.kcrason.appbasic.adapter.base

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.kcrason.appbasic.adapter.viewholder.BaseListViewHolder
import kotlin.collections.ArrayList

abstract class BaseListAdapter<T>(context: Context) : BaseAdapter() {
    val arrayData: ArrayList<T> = ArrayList()
    val mContext: Context = context

    fun addDataToStart(newArrayData: List<T>) {
        arrayData.addAll(0, newArrayData)
        notifyDataSetChanged()
    }

    fun showLoadMoreData(newArrayData: List<T>) {
        arrayData.addAll(newArrayData)
        notifyDataSetChanged()
    }

    fun showRefreshData(newArrayData: List<T>) {
        arrayData.clear()
        arrayData.addAll(newArrayData)
        notifyDataSetChanged()
    }


    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {
        val baseListViewHolder: BaseListViewHolder
        val itemView: View?
        if (convertView == null) {
            itemView = LayoutInflater.from(mContext).inflate(getItemLayoutId(), parent, false)
            baseListViewHolder = BaseListViewHolder(itemView)
            itemView.tag = baseListViewHolder
        } else {
            itemView = convertView
            baseListViewHolder = convertView.tag as BaseListViewHolder
        }
        bindData(baseListViewHolder, if (inRangArray(position)) arrayData[position] else null, position)
        return itemView
    }

    private fun inRangArray(index: Int): Boolean {
        return (index >= 0 && index < arrayData.size)
    }


    protected abstract fun getItemLayoutId(): Int

    protected abstract fun bindData(baseListViewHolder: BaseListViewHolder, data: T?, position: Int)

    override fun getItem(position: Int): T? = arrayData[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getCount(): Int = arrayData.size
}