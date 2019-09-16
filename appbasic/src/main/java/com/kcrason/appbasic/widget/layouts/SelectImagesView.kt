package com.kcrason.appbasic.widget.layouts

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.recyclerview.widget.GridLayoutManager
import com.kcrason.appbasic.R
import com.kcrason.appbasic.adapter.SelectImagesRecyclerAdapter
import kotlinx.android.synthetic.main.view_select_images.view.*
import org.jetbrains.anko.dip

/**
 * @author KCrason
 * @date 2019/8/30 10:15
 * @description
 */
class SelectImagesView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : RelativeLayout(context, attrs, defStyleAttr) {

    private var mSelectImagesRecyclerAdapter: SelectImagesRecyclerAdapter? = null

    init {
        LayoutInflater.from(context).inflate(R.layout.view_select_images, this, true)
        val typeValue = context.obtainStyledAttributes(attrs, R.styleable.SelectImagesView)
        val gridCount = typeValue.getInt(R.styleable.SelectImagesView_gridCount, 3)
        val itemGap =
            typeValue.getDimensionPixelSize(R.styleable.SelectImagesView_itemGap, context.dip(4))

        val supportStartAdd =
            typeValue.getBoolean(R.styleable.SelectImagesView_supportStartAdd, false)

        val parentMarginLeft = typeValue.getDimensionPixelSize(
            R.styleable.SelectImagesView_parentMarginLeft,
            context.dip(16)
        )

        val parentMarginRight = typeValue.getDimensionPixelSize(
            R.styleable.SelectImagesView_parentMarginLeft,
            context.dip(16)
        )

        val parentMarginTop = typeValue.getDimensionPixelSize(
            R.styleable.SelectImagesView_parentMarginTop,
            context.dip(0)
        )

        val parentMarginBottom = typeValue.getDimensionPixelSize(
            R.styleable.SelectImagesView_parentMarginBottom,
            context.dip(0)
        )

        val itemParams =
            (context.resources.displayMetrics.widthPixels - ((gridCount - 1) * itemGap) -
                    paddingLeft - paddingRight - parentMarginLeft - parentMarginRight) / gridCount

        val params = LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        params.leftMargin = parentMarginLeft
        params.rightMargin = parentMarginRight
        params.topMargin = parentMarginTop
        params.bottomMargin = parentMarginBottom
        selectImageRecyclerView.layoutParams = params

        typeValue.recycle()
        selectImageRecyclerView.layoutManager = GridLayoutManager(context, gridCount)

        mSelectImagesRecyclerAdapter =
            SelectImagesRecyclerAdapter(context, gridCount, itemGap, itemParams, supportStartAdd)
        selectImageRecyclerView.adapter = mSelectImagesRecyclerAdapter
    }

    fun setOnClickDeleteImageListener(onClickDeleteImageListener: ((Int) -> Unit)?): SelectImagesView {
        this.mSelectImagesRecyclerAdapter?.setOnClickDeleteImageListener(onClickDeleteImageListener)
        return this
    }

    fun getSelectedImages(): List<String>? {
        return mSelectImagesRecyclerAdapter?.arrayData
    }

    fun setMaxCount(maxCount: Int) {
        mSelectImagesRecyclerAdapter?.setMaxCount(maxCount)
    }

    fun setOnClickAddImagesListener(onClickAddImagesListener: ((Int) -> Unit)? = null): SelectImagesView {
        this.mSelectImagesRecyclerAdapter?.setOnClickAddImagesListener(onClickAddImagesListener)
        return this
    }

    fun currentImageCount(): Int {
        return mSelectImagesRecyclerAdapter?.arrayData?.size ?: 0
    }

    fun showSelectedImages(imageUrls: List<String>?) {
        if (imageUrls.isNullOrEmpty()) {
            return
        } else {
            mSelectImagesRecyclerAdapter?.arrayData?.addAll(imageUrls)
            mSelectImagesRecyclerAdapter?.notifyDataSetChanged()
        }
    }
}