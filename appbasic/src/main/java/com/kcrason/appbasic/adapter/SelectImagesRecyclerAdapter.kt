package com.kcrason.appbasic.adapter

import android.content.Context
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.kcrason.appbasic.adapter.base.SimpleRecyclerAdapter
import com.kcrason.appbasic.adapter.viewholder.BaseRecyclerViewHolder
import com.kcrason.appbasic.common.showImageOfUrl
import com.kcrason.appbasic.R
import com.kcrason.appbasic.ui.activity.PreviewImagesActivity
import kotlinx.android.synthetic.main.item_recycler_select_images_view.view.*
import org.jetbrains.anko.dip

/**
 * @author KCrason
 * @date 2019/8/30 10:16
 * @description
 */
class SelectImagesRecyclerAdapter(
    val context: Context,
    private val gridCount: Int,
    itemGap: Int,

    itemParams: Int, private val supportStartAdd: Boolean
) :
    SimpleRecyclerAdapter<String>(context, R.layout.item_recycler_select_images_view) {

    private val mItemMargin = itemGap
    private var mItemParams: Int = itemParams
    private var mMaxCount: Int = 9
    private var mOnClickAddImagesListener: ((Int) -> Unit)? = null
    private var mOnClickDeleteImageListener: ((Int) -> Unit)? = null

    fun setOnClickDeleteImageListener(onClickDeleteImageListener: ((Int) -> Unit)?) {
        this.mOnClickDeleteImageListener = onClickDeleteImageListener
    }

    fun setOnClickAddImagesListener(onClickAddImagesListener: ((Int) -> Unit)? = null) {
        this.mOnClickAddImagesListener = onClickAddImagesListener
    }

    fun setMaxCount(maxCount: Int) {
        this.mMaxCount = maxCount
    }

    override fun getItemCount(): Int {
        if (arrayData.size <= 0) {
            return if (supportStartAdd) 1 else 0
        }
        if (arrayData.size >= mMaxCount) {
            return mMaxCount
        }
        return super.getItemCount() + 1
    }

    override fun bindData(
        baseRecyclerViewHolder: BaseRecyclerViewHolder,
        data: String?,
        position: Int
    ) {
        val layoutParams = RecyclerView.LayoutParams(mItemParams, mItemParams)
        layoutParams.leftMargin = 0
        layoutParams.rightMargin =
            if (position == arrayData.size - 1) 0 else mItemMargin
        layoutParams.topMargin = 0
        layoutParams.bottomMargin = mItemMargin
        baseRecyclerViewHolder.itemView.layoutParams = layoutParams

        baseRecyclerViewHolder.itemView.imgDelete.setOnClickListener {
            val adapterPosition = baseRecyclerViewHolder.adapterPosition
            if (inRangArray(adapterPosition)) {
                arrayData.removeAt(adapterPosition)
            }
            if (supportStartAdd) {
                notifyItemRemoved(adapterPosition)
            } else {
                if (arrayData.size > 0) {
                    notifyItemRemoved(adapterPosition)
                } else {
                    notifyDataSetChanged()
                }
            }
            mOnClickDeleteImageListener?.invoke(arrayData.size)
        }

        baseRecyclerViewHolder.itemView.setOnClickListener {
            if (baseRecyclerViewHolder.adapterPosition == itemCount - 1 && arrayData.size < 9) {
                mOnClickAddImagesListener?.invoke(arrayData.size)
            } else {
                PreviewImagesActivity.startPreviewImages(
                    context,
                    arrayData,
                    baseRecyclerViewHolder.adapterPosition
                )
            }
        }

        val imagePreView = baseRecyclerViewHolder.itemView.imagePreview

        if (baseRecyclerViewHolder.adapterPosition == itemCount - 1 && arrayData.size < 9) {
            //lastPosition
            baseRecyclerViewHolder.itemView.imgDelete.visibility = View.GONE
            imagePreView.setImageResource(R.drawable.svg_ic_add_images)
            val padding = context.dip(16 * 6 / gridCount)
            imagePreView.setPadding(padding, padding, padding, padding)
        } else {
            baseRecyclerViewHolder.itemView.imgDelete.visibility = View.VISIBLE
            imagePreView.showImageOfUrl(data)
            imagePreView.setPadding(0, 0, 0, 0)
        }
    }
}