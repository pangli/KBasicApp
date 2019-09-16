package com.kcrason.appbasic.ui.activity.base

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.MotionEvent
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.kcrason.appbasic.common.Extra
import com.kcrason.appbasic.common.checkContextNotFinishing
import com.kcrason.appbasic.common.helper.AppManager
import com.kcrason.appbasic.common.helper.CompositeDisposableHelper
import com.kcrason.appbasic.common.helper.Values
import com.kcrason.appbasic.mvp.presenter.base.BasePresenter
import com.kcrason.appbasic.mvp.view.base.BaseView
import com.kcrason.appbasic.widget.dialog.LoadingDialog
import com.kcrason.appbasic.widget.popupwindow.MultiStatusTip
import com.github.anzewei.parallaxbacklayout.ParallaxBack
import com.kcrason.appbasic.R
import com.yalantis.ucrop.UCrop
import com.zhihu.matisse.Matisse

@ParallaxBack
abstract class BaseActivity<T : BasePresenter<V>, V : BaseView> : AppCompatActivity() {

    private var mLastMotionEvent: MotionEvent? = null

    private var onFastForResultListener: ((requestCode: Int, resultCode: Int, data: Intent?) -> Unit)? =
        null

    private var mFastSelectImagesListener: ((imagePathResult: List<String>?, imageUriResult: List<Uri>?) -> Unit)? =
        null

    private var mFastCropPictureListener: ((cropResult: Uri?) -> Unit)? = null

    /**
     * Rxjava请求管理
     */
    val mCompositeDisposableHelper = CompositeDisposableHelper()

    private var mBasePresenter: T? = null

    private var mLoadingDialog: LoadingDialog? = null

    companion object {
        init {
            AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
        }
    }


    fun getLastMotionEvent(): MotionEvent? {
        return mLastMotionEvent
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutId())
        AppManager.attach(this)
        mBasePresenter = onCreatePresenter()
        init()
    }

    fun showLoadingView(
        message: String = getString(R.string.loading),
        lazyShow: (() -> Unit)? = null
    ) {
        if (mLoadingDialog == null) {
            mLoadingDialog = loadingDialog(this, message)
        } else {
            mLoadingDialog?.setMessage(message)
        }
        if (mLoadingDialog?.isShowing == false) {
            mLoadingDialog?.show()
        }
        lazyShow?.invoke()
    }

    fun dismissLoadingView() {
        if (mLoadingDialog?.isShowing == true) {
            mLoadingDialog?.dismiss()
        }
    }

    /**
     * 显示加载dialog
     */
    private fun loadingDialog(context: Context?, message: String): LoadingDialog? {
        var loadingDialog: LoadingDialog? = null
        context.checkContextNotFinishing { safeContext ->
            loadingDialog = LoadingDialog(safeContext).setMessage(message)
        }
        return loadingDialog
    }


    override fun onDestroy() {
        super.onDestroy()
        dismissLoadingView()
        AppManager.detach(this)
        mCompositeDisposableHelper.clearRequest()
        mBasePresenter?.clearBaseView()
        mBasePresenter = null
    }

    fun getPresenter(): T {
        if (mBasePresenter == null) {
            mBasePresenter = onCreatePresenter()
        }
        return mBasePresenter as T
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        this.mLastMotionEvent = ev
        return super.dispatchTouchEvent(ev)
    }


    fun fastSelectImages(
        maxSelectable: Int,
        fastSelectImagesListener: ((imagePathResult: List<String>?, imageUriResult: List<Uri>?) -> Unit)?
    ) {
        this.mFastSelectImagesListener = fastSelectImagesListener
        Extra.innerSelectImages(this, maxSelectable)
    }


    fun fastStartForResultActivity(
        intent: Intent,
        requestCode: Int,
        onFastForResultListener: ((requestCode: Int, resultCode: Int, data: Intent?) -> Unit)?
    ) {
        this.onFastForResultListener = onFastForResultListener
        startActivityForResult(intent, requestCode)
    }

    fun fastCropPicture(pictureUri: Uri, fastCropPictureListener: ((cropResult: Uri?) -> Unit)?) {
        this.mFastCropPictureListener = fastCropPictureListener
        Extra.cropPicture(this, pictureUri)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        this.onFastForResultListener?.invoke(requestCode, resultCode, data)
        if (requestCode == Values.RequestCode.REQUEST_MATISSE) {
            if (resultCode == Activity.RESULT_OK) {
                mFastSelectImagesListener?.invoke(
                    Matisse.obtainPathResult(data),
                    Matisse.obtainResult(data)
                )
            } else {
                mFastSelectImagesListener?.invoke(null, null)
            }
        }
        if (requestCode == UCrop.REQUEST_CROP && data != null) {
            if (resultCode == Activity.RESULT_OK) {
                mFastCropPictureListener?.invoke(UCrop.getOutput(data))
            } else {
                MultiStatusTip.showWarnTip(this, "图片裁剪失败:${UCrop.getError(data)}")
            }
        }
    }

    protected abstract fun onCreatePresenter(): T

    protected abstract fun init()

    protected abstract fun getLayoutId(): Int

}