package com.kcrason.appbasic.adapter.base

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter

abstract class BasePagerAdapter<T>(private var arrayList: List<T>, private val layoutId: Int) :
    PagerAdapter() {

    val arrayData get() = arrayList

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        if (position < arrayList.size) {
            val itemView = LayoutInflater.from(container.context)
                .inflate(layoutId, container, false)
            bindView(itemView, arrayList[position], position)
            container.addView(itemView)
            return itemView
        }
        return super.instantiateItem(container, position)
    }


    abstract fun bindView(itemView: View, data: T, position: Int)

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    override fun getCount(): Int {
        return arrayList.size
    }
}