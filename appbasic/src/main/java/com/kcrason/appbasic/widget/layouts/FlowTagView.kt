package com.kcrason.appbasic.widget.layouts

import android.content.Context
import android.database.DataSetObserver
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.kcrason.appbasic.R
import com.kcrason.appbasic.common.SimpleWeakObjectPool
import kotlin.math.max

/**
 * @author KCrason
 * @date 2019/6/11 18:10
 * @description
 */
class FlowTagView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ViewGroup(context, attrs, defStyleAttr), ViewGroup.OnHierarchyChangeListener {
    override fun onChildViewRemoved(parent: View?, child: View?) {
        child?.let { simpleWeakObjectPool?.put(it) }
    }

    override fun onChildViewAdded(parent: View?, child: View?) {
    }

    private var simpleWeakObjectPool: SimpleWeakObjectPool<View>? = null

    private var mLineSpacing: Int = 0
    private var mTagSpacing: Int = 0
    private var mAdapter: BaseAdapter? = null

    val adapter: BaseAdapter? get() = mAdapter

    init {
        simpleWeakObjectPool = SimpleWeakObjectPool(5)
        setOnHierarchyChangeListener(this)
        val a = context.obtainStyledAttributes(attrs, R.styleable.FlowTagView)
        mLineSpacing = a.getDimensionPixelSize(R.styleable.FlowTagView_lineSpacing, 5)
        mTagSpacing = a.getDimensionPixelSize(R.styleable.FlowTagView_tagSpacing, 10)
        a.recycle()
    }


    fun notifyDataSetChanged() {
        drawLayout()
    }

    private fun drawLayout() {
        if (mAdapter?.count ?: 0 == 0) {
            this.removeAllViews()
            return
        }
        val oldCount = childCount
        val newCount = mAdapter?.count ?: 0
        if (newCount < oldCount) {
            removeViewsInLayout(newCount, oldCount - newCount)
        }
        for (i in 0 until newCount) {
            val hasChild = i < oldCount
            // 简单的回收机制,主要是为ListView/RecyclerView做优化
            var recycleView = if (hasChild) getChildAt(i) else null
            if (recycleView == null) {
                recycleView = simpleWeakObjectPool?.get()
                val child = mAdapter?.getView(i, recycleView, this)
                addViewInLayout(
                    child,
                    i,
                    child?.layoutParams ?: LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT),
                    true
                )
            } else {
                mAdapter?.getView(i, recycleView, this)
            }
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        var wantHeight = 0
        val wantWidth = View.resolveSize(0, widthMeasureSpec)
        val paddingLeft = paddingLeft
        val paddingRight = paddingRight
        val paddingTop = paddingTop
        val paddingBottom = paddingBottom
        var childLeft = paddingLeft
        var childTop = paddingTop
        var lineHeight = 0

        //固定列的数量所需要的代码
        for (i in 0 until childCount) {
            val childView = getChildAt(i)
            val params = childView.layoutParams
            childView.measure(
                getChildMeasureSpec(widthMeasureSpec, paddingLeft + paddingRight, params.width),
                getChildMeasureSpec(heightMeasureSpec, paddingTop + paddingBottom, params.height)
            )
            //获取单个tag的宽高
            val childHeight = childView.measuredHeight
            val childWidth = childView.measuredWidth
            lineHeight = Math.max(childHeight, lineHeight)

            //超过长度的新起一行
            if (childLeft + childWidth + paddingRight > wantWidth) {
                childLeft = paddingLeft
                childTop += mLineSpacing + childHeight
                lineHeight = childHeight
            }
            childLeft += childWidth + mTagSpacing
        }
        wantHeight += childTop + lineHeight + paddingBottom
        setMeasuredDimension(wantWidth, View.resolveSize(wantHeight, heightMeasureSpec))
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        val width = r - l
        val paddingLeft = paddingLeft
        val paddingTop = paddingTop
        val paddingRight = paddingRight
        var childLeft = paddingLeft
        var childTop = paddingTop
        var lineHeight = 0

        for (i in 0 until childCount) {
            val childView = getChildAt(i)
            if (childView.visibility == View.GONE) {
                continue
            }
            val childWidth = childView.measuredWidth
            val childHeight = childView.measuredHeight
            lineHeight = max(childHeight, lineHeight)

            if (childLeft + childWidth + paddingRight > width) {
                childLeft = paddingLeft
                childTop += mLineSpacing + lineHeight
                lineHeight = childHeight
            }

            childView.layout(childLeft, childTop, childLeft + childWidth, childTop + childHeight)
            childLeft += childWidth + mTagSpacing
        }
    }

    override fun generateLayoutParams(attrs: AttributeSet): LayoutParams {
        return LayoutParams(this.context, attrs)
    }


    fun setAdapter(adapter: BaseAdapter) {
        mAdapter = adapter
        mAdapter?.registerDataSetObserver(object : DataSetObserver() {
            override fun onChanged() {
                this@FlowTagView.drawLayout()
            }
        })
        drawLayout()
    }
}