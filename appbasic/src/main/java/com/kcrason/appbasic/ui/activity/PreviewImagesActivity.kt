package com.kcrason.appbasic.ui.activity

import android.content.Context
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.kcrason.appbasic.adapter.base.SimpleRecyclerAdapter
import com.kcrason.appbasic.adapter.viewholder.BaseRecyclerViewHolder
import com.kcrason.appbasic.common.showImageOfUrl
import com.kcrason.appbasic.mvp.presenter.base.BasePresenter
import com.kcrason.appbasic.mvp.view.base.BaseView
import com.fid.basic.ui.activity.base.BaseTitleBarActivity
import com.kcrason.appbasic.R
import kotlinx.android.synthetic.main.activity_preview_images.*
import kotlinx.android.synthetic.main.item_recycler_preview_images_view.view.*
import org.jetbrains.anko.startActivity

class PreviewImagesActivity : BaseTitleBarActivity<BasePresenter<BaseView>, BaseView>(), BaseView {

    companion object {
        fun startPreviewImages(context: Context, imagePathResult: List<String>, previewPage: Int) {
            context.startActivity<PreviewImagesActivity>(
                "key_preview_images" to imagePathResult,
                "key_preview_page" to previewPage
            )
        }
    }


    override fun initialize() {
        previewRecyclerView.layoutManager =
            LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        val pagerSnapHelper = PagerSnapHelper()
        pagerSnapHelper.attachToRecyclerView(previewRecyclerView)
        val adapter = object :
            SimpleRecyclerAdapter<String>(this, R.layout.item_recycler_preview_images_view) {
            override fun bindData(
                baseRecyclerViewHolder: BaseRecyclerViewHolder,
                data: String?,
                position: Int
            ) {
                baseRecyclerViewHolder.itemView.photoView.showImageOfUrl(data)
            }
        }
        previewRecyclerView.adapter = adapter
        val previewImages = intent.getStringArrayListExtra("key_preview_images")
        val previewPage = intent.getIntExtra("key_preview_page", 0)
        previewImages?.let {
            adapter.showRefreshData(previewImages)
        }
        previewRecyclerView.scrollToPosition(previewPage)
    }

    override fun getContainerLayout(): Int {
        return R.layout.activity_preview_images
    }

    override fun getTopTitle(): String? {
        return "图片预览"
    }

    override fun onCreatePresenter(): BasePresenter<BaseView> {
        return BasePresenter(this, this)
    }
}