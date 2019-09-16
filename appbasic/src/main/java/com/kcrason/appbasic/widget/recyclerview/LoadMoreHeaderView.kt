package com.kcrason.appbasic.widget.recyclerview

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.kcrason.appbasic.R
import com.kcrason.appbasic.common.helper.NetworkHelper
import kotlinx.android.synthetic.main.view_recycler_load_more_footer.view.*

/**
 * @author KCrason
 * @date 2019/8/5 11:38
 * @description
 */
class LoadMoreHeaderView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    init {
        val itemView = LayoutInflater.from(context)
            .inflate(R.layout.view_recycler_load_more_header, this, false)
        val itemViewLayoutParam: RecyclerView.LayoutParams =
            RecyclerView.LayoutParams(LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        this.layoutParams = itemViewLayoutParam
        addView(itemView)
    }

    fun showLoadingView() {
        pb_loading.visibility = View.VISIBLE
        txt_message.visibility = View.GONE
    }

    fun hideFooterView() {
        pb_loading.visibility = View.GONE
        txt_message.visibility = View.GONE
    }

    fun loadErrorText() {
        txt_message.visibility = View.VISIBLE
        pb_loading.visibility = View.GONE
        txt_message.text =
            if (NetworkHelper.isNetWorkAvailable(context)) context.getString(R.string.get_more_data_failure) else  context.getString(
                R.string.not_network
            )
    }

    fun allLoadCompleteText(description: String) {
        txt_message.visibility = View.VISIBLE
        pb_loading.visibility = View.GONE
        txt_message.text = description
    }

    fun allLoadCompleteGone() {
        txt_message.visibility = View.GONE
        pb_loading.visibility = View.GONE
    }
}