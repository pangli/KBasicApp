package com.kcrason.appbasic.widget.layouts

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import com.facebook.shimmer.ShimmerFrameLayout
import com.kcrason.appbasic.common.helper.NetworkHelper
import com.kcrason.appbasic.widget.popupwindow.MultiStatusTip
import com.kcrason.appbasic.R
import kotlinx.android.synthetic.main.view_multi_state_load_failure.view.*
import kotlinx.android.synthetic.main.view_multi_state_not_data.view.*

/**
 * @author KCrason
 * @date 2019/5/22 14:08
 * @description
 */
class MultiStateView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private var mLoadingView: View? = null
    private var mNotDataView: View? = null
    private var mLoadFailureView: View? = null
    private var mShimmerPreview: View? = null


    fun shimmerPreview(previewLayout: Int, callback: (() -> Unit)? = null) {
        removeOtherLayout()
        if (mShimmerPreview == null) {
            mShimmerPreview =
                LayoutInflater.from(context).inflate(R.layout.view_multi_state_shimmer_preview, this, false)
        }
        if (mShimmerPreview?.parent == null) {
            val shimmerView = mShimmerPreview?.findViewById<ShimmerFrameLayout>(R.id.shimmer)
            shimmerView?.addView(LayoutInflater.from(context).inflate(previewLayout, shimmerView, false))
            addView(mShimmerPreview)
            shimmerView?.startShimmer()
            callback?.invoke()
        }
    }

    fun notData(description: String = context.getString(R.string.not_data)) {
        removeOtherLayout()
        if (mNotDataView == null) {
            mNotDataView = LayoutInflater.from(context).inflate(R.layout.view_multi_state_not_data, this, false)
        }
        if (mNotDataView?.parent == null) {
            addView(mNotDataView)
            txtDescription.text = description
        }
    }


    fun loading(loadingListener: (() -> Unit)) {
        removeOtherLayout()
        if (mLoadingView == null) {
            mLoadingView = LayoutInflater.from(context).inflate(R.layout.view_multi_state_loading, this, false)
        }
        if (mLoadingView?.parent == null) {
            addView(mLoadingView)
            loadingListener.invoke()
        }
    }


    /**
     * 加载失败，包括没有网络。
     */
    fun loadFailure(shimmerLayoutId: Int = 0, onClickRetryListener: (() -> Unit)) {
        removeOtherLayout()
        if (mLoadFailureView == null) {
            mLoadFailureView = LayoutInflater.from(context).inflate(R.layout.view_multi_state_load_failure, this, false)
        }
        if (mLoadFailureView?.parent == null) {
            addView(mLoadFailureView)
            layoutLoadFailure.setOnClickListener {
                if (NetworkHelper.isNetWorkAvailable(context)) {
                    if (shimmerLayoutId == 0) {
                        loading { onClickRetryListener.invoke() }
                    } else {
                        shimmerPreview(shimmerLayoutId) {
                            onClickRetryListener.invoke()
                        }
                    }
                } else {
                    MultiStatusTip.showWarnTip(context, "请打开网络后重试")
                }
            }
        }
    }

    fun showContent() {
        removeOtherLayout()
    }

    private fun removeOtherLayout() {
        mLoadingView?.parent?.let { removeView(mLoadingView) }
        mNotDataView?.parent?.let { removeView(mNotDataView) }
        mLoadFailureView?.parent?.let { removeView(mLoadFailureView) }
        mShimmerPreview?.parent?.let { removeView(mShimmerPreview) }
    }
}