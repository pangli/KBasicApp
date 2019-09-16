package com.kcrason.appbasic.widget.layouts

import android.content.Context
import android.util.AttributeSet
import android.util.SparseArray
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.kcrason.appbasic.common.SimpleWeakObjectPool

/**
 * @author KCrason
 * @date 2019/5/23 16:27
 * @description
 */
class NineGridView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) : ViewGroup(context, attrs),
    ViewGroup.OnHierarchyChangeListener {
    private var mAdapter: NineGridAdapter<*>? = null
    private var mListener: OnImageClickListener? = null
    private var mRows: Int = 0
    private var mColumns: Int = 0
    private var space: Int = 0
    private var childWidth: Int = 0
    private var childHeight: Int = 0

    private var simpleWeakObjectPool: SimpleWeakObjectPool<View>? = null

    private var mSingleWidth: Int = 0
    private var mSingleHeight: Int = 0


    private var mImageViews: SparseArray<ImageView>? = null

    val imageViews: SparseArray<ImageView>?
        get() = mImageViews

    init {
        setOnHierarchyChangeListener(this)
        simpleWeakObjectPool = SimpleWeakObjectPool(5)
        space = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            4f, context.resources.displayMetrics
        ).toInt()
    }

    fun setSingleImageSize(width: Int, height: Int): NineGridView {
        this.mSingleWidth = width
        this.mSingleHeight = height
        return this
    }

    fun setAdapter(adapter: NineGridAdapter<*>): NineGridView {
        if (adapter.getCount() <= 0) {
            removeAllViews()
            return this
        }
        if (mImageViews == null) {
            mImageViews = SparseArray()
        } else {
            mImageViews?.clear()
        }
        mAdapter = adapter
        val oldCount = childCount
        val newCount = adapter.getCount()
        initMatrix(newCount)
        removeScrapViews(oldCount, newCount)
        addChildrenData(adapter)
        requestLayout()
        return this
    }

    private fun removeScrapViews(oldCount: Int, newCount: Int) {
        if (newCount < oldCount) {
            removeViewsInLayout(newCount, oldCount - newCount)
        }
    }

    private fun initMatrix(length: Int) {
        if (length <= 3) {
            mRows = 1
            mColumns = length
        } else if (length <= 6) {
            mRows = 2
            mColumns = 3 // 因为length <=6 所以实际Columns<3也是不会导致计算出问题的
            if (length == 4) {
                mColumns = 2
            }
        } else {
            mRows = 3
            mColumns = 3
        }
    }

    private fun addChildrenData(adapter: NineGridAdapter<*>) {
        val childCount = childCount
        val count = adapter.getCount()
        for (i in 0 until count) {
            val hasChild = i < childCount
            // 简单的回收机制,主要是为ListView/RecyclerView做优化
            var recycleView: View? = if (hasChild) getChildAt(i) else null
            if (recycleView == null) {
                recycleView = simpleWeakObjectPool?.get()
                val child = adapter.getView(i, recycleView)
                addViewInLayout(child, i, child.layoutParams, true)
                mImageViews?.put(i, child as ImageView)
            } else {
                adapter.getView(i, recycleView)
                mImageViews?.put(i, recycleView as ImageView)
            }
        }
    }

    override fun addViewInLayout(
        child: View,
        index: Int,
        params: LayoutParams,
        preventRequestLayout: Boolean
    ): Boolean {
        if (child !is ImageView) {
            throw ClassCastException("addView(View child) NineGridView只能放ImageView")
        }
        return super.addViewInLayout(child, index, params, preventRequestLayout)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val childCount = childCount
        if (childCount <= 0) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec)
            return
        }

        if ((mRows == 0 || mColumns == 0) && mAdapter == null) {
            initMatrix(childCount)
        }

        val minW = paddingLeft + paddingRight + suggestedMinimumWidth
        val width = View.resolveSizeAndState(minW, widthMeasureSpec, 1)
        val availableWidth = width - paddingLeft - paddingRight
        if (childCount <= 1) {
            childWidth = if (mSingleWidth == 0) availableWidth * 2 / 4 else {
                availableWidth / 2
            }
            childHeight = if (mSingleHeight == 0) childWidth else {
                (mSingleHeight / mSingleWidth.toFloat() * childWidth).toInt()
            }
        } else {
            childWidth = (availableWidth - space * (mColumns - 1)) / mColumns
            childHeight = childWidth
        }
        val height = childHeight * mRows + space * (mRows - 1)
        setMeasuredDimension(width, height + paddingTop + paddingBottom)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        layoutChildren()
    }

    private fun layoutChildren() {
        if (mRows <= 0 || mColumns <= 0) {
            return
        }

        val childCount = childCount
        for (i in 0 until childCount) {
            val view = getChildAt(i) as ImageView

            val row = i / mColumns
            val col = i % mColumns
            val left = (childWidth + space) * col + paddingLeft
            val top = (childHeight + space) * row + paddingTop
            val right = left + childWidth
            val bottom = top + childHeight
            view.layout(left, top, right, bottom)
            view.setOnClickListener {
                mListener?.onImageClick(i, view)
            }
        }
    }


    fun setOnImageClickListener(listener: OnImageClickListener): NineGridView {
        mListener = listener
        return this
    }

    override fun onChildViewAdded(parent: View, child: View) {

    }

    override fun onChildViewRemoved(parent: View, child: View) {
        simpleWeakObjectPool?.put(child)
    }

}


interface NineGridAdapter<T> {
    fun getCount(): Int
    fun getView(position: Int, itemView: View?): View
}

interface OnImageClickListener {
    fun onImageClick(position: Int, view: View)
}
