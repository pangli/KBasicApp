package com.kcrason.appbasic.widget.others

import android.content.Context
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.kcrason.appbasic.widget.shadow.ShadowLayout
import com.kcrason.appbasic.R
import org.jetbrains.anko.dip

/**
 * @author KCrason
 * @date 2019/6/13 9:48
 * @description RecyclerView拖拽帮助
 */
class ItemMoveCallBackHelper(
    private val context: Context,
    itemMoveHelperListener: ((fromPosition: Int, toPosition: Int) -> Unit)
) :
    ItemTouchHelper.Callback() {

    private var mItemMoveHelperListener: ((fromPosition: Int, toPosition: Int) -> Unit) =
        itemMoveHelperListener

    override fun getMovementFlags(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder
    ): Int {
        val dragFlags =
            ItemTouchHelper.UP or ItemTouchHelper.DOWN
        return makeMovementFlags(dragFlags, 0)
    }


    override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
        super.onSelectedChanged(viewHolder, actionState)
        if (actionState != ItemTouchHelper.ACTION_STATE_IDLE) {
            viewHolder?.itemView?.let {
                if (it is ShadowLayout) {
                    it.setShadowLimit(context.dip(5))
                } else {
                    it.setBackgroundColor(
                        ContextCompat.getColor(
                            it.context,
                            R.color.color_f0f2f5
                        )
                    )
                }
            }
        }
    }

    override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
        super.clearView(recyclerView, viewHolder)
        val itemView = viewHolder.itemView
        if (itemView is ShadowLayout) {
            itemView.setShadowLimit(0)
        } else {
            itemView.setBackgroundColor(0)
        }
    }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        mItemMoveHelperListener.invoke(viewHolder.adapterPosition, target.adapterPosition)
        return true
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
    }


    override fun isItemViewSwipeEnabled(): Boolean {
        return false
    }

    override fun isLongPressDragEnabled(): Boolean {
        return false
    }
}