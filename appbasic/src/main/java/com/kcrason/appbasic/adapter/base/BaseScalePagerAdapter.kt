package com.kcrason.appbasic.adapter.base

import android.content.Context
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.viewpager.widget.PagerAdapter

/**
 * @author KCrason
 * @date 2019/6/26 14:50
 * @description
 */
abstract class BaseScalePagerAdapter<T>(var context: Context, private val arrayData: List<T>?) : PagerAdapter(),
    CardAdapter {

    val cardViewSparseArray = SparseArray<CardView>()

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    override fun getCount(): Int {
        return if (arrayData == null) 0 else Integer.MAX_VALUE
    }

    override fun getBaseElevation(): Float {
        return 3f
    }


    override fun getCardViewAt(position: Int): CardView {
        return cardViewSparseArray.get(position)
    }

    /**
     * 检查当前索引是否在指定数组范围
     */
    fun inRangArray(index: Int): Boolean {
        return arrayData?.let { return (index >= 0 && index < it.size) } ?: false
    }


    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val realPosition = getRealPosition(position)
        if (arrayData != null && realPosition < arrayData.size) {
            val view =
                LayoutInflater.from(container.context).inflate(getItemLayoutId(), container, false)
            container.addView(view)
            bindData(arrayData[realPosition], view, position)
            return view
        }
        return container
    }


    abstract fun getItemLayoutId(): Int

    abstract fun bindData(data: T?, view: View, position: Int)

    private fun getRealPosition(position: Int): Int {
        return position % arrayData!!.size
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
        cardViewSparseArray.put(position, null)
    }
}

interface CardAdapter {

    fun getBaseElevation(): Float

    fun getCount(): Int

    fun getCardViewAt(position: Int): CardView

    companion object {
        const val MAX_ELEVATION_FACTOR = 8
    }
}
