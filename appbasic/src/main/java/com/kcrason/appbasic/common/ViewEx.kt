package com.kcrason.appbasic.common

import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.widget.AbsListView
import android.widget.EditText
import android.widget.ImageView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.kcrason.appbasic.network.glide.GlideApp

/**
 * @author KCrason
 * @date 2019/6/10 18:02
 * @description 相关扩展函数
 */


/**
 * EditText扩展函数： afterTextChanged扩展函数
 */
inline fun EditText.afterTextChanged(crossinline afterTextChanged: (String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            afterTextChanged.invoke(s.toString())
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        }
    })
}

/**
 * AbsListView扩展函数： onScrollStateChanged简化函数
 */
inline fun AbsListView.onScrollStateChanged(crossinline onScrollStateChanged: (AbsListView?, Int) -> Unit) {
    this.setOnScrollListener(object : AbsListView.OnScrollListener {
        override fun onScrollStateChanged(view: AbsListView?, scrollState: Int) {
            onScrollStateChanged(view, scrollState)
        }

        override fun onScroll(
            view: AbsListView?,
            firstVisibleItem: Int,
            visibleItemCount: Int,
            totalItemCount: Int
        ) {
        }
    })
}


/**
 * SwipeRefreshLayout扩展函数：取消自动刷新
 */
fun SwipeRefreshLayout?.closeRefresh() {
    Handler(Looper.getMainLooper()).postDelayed({ this?.isRefreshing = false }, 200)
}

/**
 * SwipeRefreshLayout扩展函数：自动刷新
 */
inline fun SwipeRefreshLayout.autoRefresh(crossinline onAutoRefreshListener: (() -> Unit)) {
    Handler(Looper.getMainLooper()).post {
        isRefreshing = true
        onAutoRefreshListener.invoke()
    }
}


/**
 * Glide扩展函数：通过图片url显示图片
 */
fun ImageView?.showImageOfUrl(imageUrl: String?) {
    this?.let {
        GlideApp.with(it.context)
            .load(imageUrl)
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(it)
    }
}



/**
 * Glide扩展函数：通过图片url按给定大小显示图片
 */
fun ImageView?.showImageOfSize(imageUrl: String?, imageWidth: Int, imageHeight: Int) {
    this?.let {
        GlideApp.with(it.context)
            .load(imageUrl)
            .apply(RequestOptions.overrideOf(imageWidth, imageHeight))
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(it)
    }
}



