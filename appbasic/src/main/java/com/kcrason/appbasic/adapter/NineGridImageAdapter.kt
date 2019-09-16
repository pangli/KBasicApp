package com.kcrason.appbasic.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.ContextCompat
import com.kcrason.appbasic.widget.layouts.NineGridAdapter
import com.kcrason.appbasic.R

abstract class NineGridImageAdapter<T>(context: Context, private val arrayData: List<T>?) :
    NineGridAdapter<T> {
    private var mContext: Context = context

    private var mArrayData: List<T>? = arrayData

    override fun getCount(): Int {
        return arrayData?.size ?: 0
    }

    override fun getView(position: Int, itemView: View?): View {
        val imageView: ImageView
        if (itemView == null) {
            imageView = ImageView(mContext)
            imageView.setBackgroundColor(ContextCompat.getColor(mContext, R.color.color_f0f2f5))
            imageView.scaleType = ImageView.ScaleType.CENTER_CROP
            imageView.layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        } else {
            imageView = itemView as ImageView
        }
        mArrayData?.let {
            if (position < it.size) {
                showImage(it[position], imageView)
            }
        }
        return imageView
    }

    abstract fun showImage(data: T, imageView: ImageView)
}