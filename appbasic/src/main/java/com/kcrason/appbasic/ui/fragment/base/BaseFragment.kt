package com.kcrason.appbasic.ui.fragment.base

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.kcrason.appbasic.common.Extra
import com.kcrason.appbasic.common.clearAllRequest
import com.kcrason.appbasic.common.helper.Values
import com.kcrason.appbasic.mvp.presenter.base.BasePresenter
import com.kcrason.appbasic.mvp.view.base.BaseView
import com.kcrason.appbasic.widget.popupwindow.MultiStatusTip
import com.yalantis.ucrop.UCrop
import com.zhihu.matisse.Matisse

abstract class BaseFragment<T : BasePresenter<V>, V : BaseView> : Fragment() {
    private var mBasePresenter: T? = null

    private var mFastSelectImagesListener: ((imagePathResult: List<String>?, imageUriResult: List<Uri>?) -> Unit)? =
        null
    private var onFastForResultListener: ((requestCode: Int, resultCode: Int, data: Intent?) -> Unit)? =
        null

    private var mFastCropPictureListener: ((cropResult: Uri?) -> Unit)? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(getLayoutId(), container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        context.clearAllRequest()
        mBasePresenter?.clearBaseView()
        mBasePresenter = null
    }


    protected abstract fun getLayoutId(): Int

    protected abstract fun init()

    fun getPresenter(): T {
        if (mBasePresenter == null) {
            mBasePresenter = onCreatePresenter()
        }
        return mBasePresenter as T
    }


    fun fastStartForResultActivity(
        intent: Intent,
        requestCode: Int,
        onFastForResultListener: ((requestCode: Int, resultCode: Int, data: Intent?) -> Unit)?
    ) {
        this.onFastForResultListener = onFastForResultListener
        startActivityForResult(intent, requestCode)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        this.onFastForResultListener?.invoke(requestCode, resultCode, data)
        if (requestCode == Values.RequestCode.REQUEST_MATISSE && resultCode == Activity.RESULT_OK) {
            mFastSelectImagesListener?.invoke(
                Matisse.obtainPathResult(data),
                Matisse.obtainResult(data)
            )
        }
        if (requestCode == UCrop.REQUEST_CROP && data != null) {
            if (resultCode == Activity.RESULT_OK) {
                mFastCropPictureListener?.invoke(UCrop.getOutput(data))
            } else {
                MultiStatusTip.showWarnTip(this.context, "图片裁剪失败")
            }
        }
    }


    fun fastSelectImages(
        maxSelectable: Int,
        fastSelectImagesListener: ((imagePathResult: List<String>?, imageUriResult: List<Uri>?) -> Unit)?
    ) {
        this.mFastSelectImagesListener = fastSelectImagesListener
        Extra.innerSelectImages(this.context, maxSelectable)
    }


    fun fastCropPicture(pictureUri: Uri, fastCropPictureListener: ((cropResult: Uri?) -> Unit)?) {
        this.mFastCropPictureListener = fastCropPictureListener
        Extra.cropPicture(this.context, pictureUri)
    }

    protected abstract fun onCreatePresenter(): T
}



