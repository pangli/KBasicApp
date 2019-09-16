package com.kcrason.appbasic.widget.layouts

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.kcrason.appbasic.common.SimpleWeakObjectPool

/**
 * @author KCrason
 * @date 2019/6/12 9:57
 * @description 水平或垂直方向的View,该view可以像ListView一样设置adapter，设置不能的样式，并具备缓存view的能力。
 * 性能比LinearLayout更高,推荐在recyclerView或ListView中出现嵌套列表时使用。
 */
class MultiDirectionView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr), ViewGroup.OnHierarchyChangeListener {

    private var simpleWeakObjectPool: SimpleWeakObjectPool<View>? = null

    var multiDirectionAdapter: MultiDirectionAdapter? = null

    init {
        simpleWeakObjectPool = SimpleWeakObjectPool(5)
        setOnHierarchyChangeListener(this)
    }

    fun notifyDataSetChanged() {
        updateItemData()
    }

    private fun updateItemData() {
        if (multiDirectionAdapter?.getItemCount() ?: 0 == 0) {
            this.removeAllViews()
            return
        }
        val oldCount = childCount
        val newCount = multiDirectionAdapter?.getItemCount() ?: 0
        if (newCount < oldCount) {
            removeViewsInLayout(newCount, oldCount - newCount)
        }
        for (i in 0 until newCount) {
            val hasChild = i < oldCount
            var recycleView = if (hasChild) getChildAt(i) else null
            if (recycleView == null) {
                recycleView = simpleWeakObjectPool?.get()
                val child = multiDirectionAdapter?.getDirectionItemView(i, recycleView, this)
                addViewInLayout(
                    child, i, child?.layoutParams, true
                )
            } else {
                multiDirectionAdapter?.getDirectionItemView(i, recycleView, this)
            }
        }
        requestLayout()
    }

    fun setAdapter(multiDirectionAdapter: MultiDirectionAdapter?) {
        this.multiDirectionAdapter = multiDirectionAdapter
        updateItemData()
    }

    abstract class MultiDirectionAdapter {
        abstract fun getDirectionItemView(position: Int, convertView: View?, parent: ViewGroup): View
        abstract fun getItemCount(): Int
    }

    abstract class SimpleMultiDirectionAdapter<T>(arrayData: ArrayList<T>?, layoutResource: Int) :
        MultiDirectionAdapter() {

        private var mArrayData: ArrayList<T>? = arrayData

        val arrayData: ArrayList<T>?
            get() = mArrayData

        private var mLayoutResource: Int = layoutResource

        override fun getDirectionItemView(position: Int, convertView: View?, parent: ViewGroup): View {
            val itemView: View
            val defaultOpinionCommentHolder: SimpleMultiDirectionHolder
            if (convertView == null) {
                itemView = LayoutInflater.from(parent.context).inflate(mLayoutResource, parent, false)
                defaultOpinionCommentHolder =
                    SimpleMultiDirectionHolder(
                        itemView
                    )
                itemView.tag = defaultOpinionCommentHolder
            } else {
                itemView = convertView
                defaultOpinionCommentHolder = itemView.tag as SimpleMultiDirectionHolder
            }
            bindCommentItemData(defaultOpinionCommentHolder, mArrayData?.get(position), position)
            return itemView
        }

        override fun getItemCount(): Int {
            return mArrayData?.size ?: 0
        }

        abstract fun bindCommentItemData(
            holder: SimpleMultiDirectionHolder,
            data: T?,
            position: Int
        )

        class SimpleMultiDirectionHolder(val itemView: View) {
            val containerView: View = itemView
        }
    }

    override fun onChildViewRemoved(parent: View?, child: View?) {
        child?.let { simpleWeakObjectPool?.put(it) }
    }

    override fun onChildViewAdded(parent: View?, child: View?) {
    }
}