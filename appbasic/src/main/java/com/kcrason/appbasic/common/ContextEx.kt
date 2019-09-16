package com.kcrason.appbasic.common

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.bigkoo.pickerview.builder.TimePickerBuilder
import com.bigkoo.pickerview.listener.OnTimeSelectListener
import com.bumptech.glide.load.resource.bitmap.BitmapTransitionOptions
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.kcrason.appbasic.listeners.OnLoadImageProgressListener
import com.kcrason.appbasic.network.glide.ProgressGlide
import com.kcrason.appbasic.network.rxjava.RxSchedulers
import com.kcrason.appbasic.ui.activity.base.BaseActivity
import com.kcrason.appbasic.ui.fragment.base.BaseFragment
import com.kcrason.appbasic.widget.popupwindow.MultiStatusTip
import com.kcrason.appbasic.R
import com.kcrason.appbasic.network.glide.GlideApp
import io.reactivex.Single
import io.reactivex.disposables.Disposable
import org.reactivestreams.Subscription
import top.zibin.luban.Luban
import java.io.File
import java.util.*

/**
 * @author KCrason
 * @date 2019/6/26 16:30
 * @description
 */

fun Context?.toActivity(onTransformListener: ((activity: Activity) -> Unit)) {
    if (this is Activity) {
        onTransformListener.invoke(this)
    }
}

fun Context?.toBaseActivity(onTransformListener: ((baseActivity: BaseActivity<*, *>) -> Unit)) {
    if (this is BaseActivity<*, *>) {
        onTransformListener.invoke(this)
    }
}

fun Context?.checkSafeContext(onSafeContextListener: ((safeContext: Context) -> Unit)) {
    if (this != null) {
        onSafeContextListener.invoke(this)
    }
}


fun Context?.screenHeight(): Int {
    return this?.resources?.displayMetrics?.heightPixels ?: 0
}

fun Context?.screenWidth(): Int {
    return this?.resources?.displayMetrics?.widthPixels ?: 0
}


/**
 * 获取状态栏高度
 *
 * @param context 上下文
 * @return 高度
 */
fun Context?.statusBarHeight(): Int {
    var statusBarHeight = 0
    this.checkSafeContext { safeContext ->
        val resources = safeContext.resources
        val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
        statusBarHeight = resources.getDimensionPixelSize(resourceId)
    }
    return statusBarHeight
}

/**
 * Context下显示loading view
 */
fun Context.showLoading(message: String, lazyShow: (() -> Unit)?) {
    if (this is BaseActivity<*, *>) {
        this.showLoadingView(message, lazyShow)
    }
}

/**
 * Context下隐藏loading view
 */
fun Context.dismissLoading() {
    if (this is BaseActivity<*, *>) {
        this.dismissLoadingView()
    }
}


fun Context?.fastPickDate(onPickDateListener: (Date) -> Unit) {
    this.checkSafeContext { safeContext ->
        val startTime = Calendar.getInstance()
        startTime.set(1900, 0, 1)
        TimePickerBuilder(safeContext,
            OnTimeSelectListener { date, _ ->
                onPickDateListener.invoke(date)
            })
            .setDate(Calendar.getInstance())
            .setLineSpacingMultiplier(2f)
            .setCancelColor(ContextCompat.getColor(safeContext, R.color.color_fc5555))
            .setSubmitColor(ContextCompat.getColor(safeContext, R.color.color_0084ff))
            .setRangDate(startTime, Calendar.getInstance())
            .build()
            .show()
    }
}

/**
 * Context扩展函数：Context下加入Disposable请求队列
 */
fun Context?.addDisposable(disposable: Disposable) {
    if (this is BaseActivity<*, *>) {
        this.mCompositeDisposableHelper.addDisposable(disposable)
    }
}


/**
 * Context扩展函数：Context下加入Subscription请求队列
 */
fun Context?.addSubscription(subscription: Subscription) {
    if (this is BaseActivity<*, *>) {
        this.mCompositeDisposableHelper.addSubscription(subscription)
    }
}


/**
 * Context扩展函数：Context下清空所有请求
 */
fun Context?.clearAllRequest() {
    if (this is BaseActivity<*, *>) {
        this.mCompositeDisposableHelper.clearRequest()
    }
}


/**
 * Context扩展函数：判断当前Context是否已经被finish，用于在显示dialog之前调用。
 */
inline fun Context?.checkContextNotFinishing(crossinline action: ((safeContext: Context) -> Unit)) {
    if (this is Activity && !this.isFinishing) {
        action.invoke(this)
    }
}


/**
 * Context快速回调startActivityForResult
 */
fun Context.fastActivityForResult(
    intent: Intent,
    requestCode: Int,
    onActivityResultListener: (requestCode: Int, resultCode: Int, data: Intent?) -> Unit
) {
    if (this is BaseFragment<*, *>) {
        this.fastStartForResultActivity(intent, requestCode, onActivityResultListener)
    } else if (this is BaseActivity<*, *>) {
        this.fastStartForResultActivity(intent, requestCode, onActivityResultListener)
    }
}


/**
 * Context快速回调startActivityForResult
 */
fun Context.fastSelectImages(
    maxSelectable: Int,
    fastSelectImagesListener: ((imagePathResult: List<String>?, imageUriResult: List<Uri>?) -> Unit)?
) {
    if (this is BaseFragment<*, *>) {
        this.fastSelectImages(maxSelectable, fastSelectImagesListener)
    } else if (this is BaseActivity<*, *>) {
        this.fastSelectImages(maxSelectable, fastSelectImagesListener)
    }
}

/**
 * Context快速回调startActivityForResult
 */
fun Context.fastCropPicture(
    pictureUri: Uri, fastCropPictureListener: ((cropResult: Uri?) -> Unit)?
) {
    if (this is BaseFragment<*, *>) {
        this.fastCropPicture(pictureUri, fastCropPictureListener)
    } else if (this is BaseActivity<*, *>) {
        this.fastCropPicture(pictureUri, fastCropPictureListener)
    }
}

fun Context.fileToUri(file: File): Uri {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        FileProvider.getUriForFile(this, "com.fid.basicmodule.fileprovider", file)
    } else {
        Uri.fromFile(file)
    }
}


fun Context?.fastCompressImagesByUri(
    imageUriArray: List<Uri>?,
    onCompressResult: (List<File>?) -> Unit
) {
    this.checkSafeContext { safeContext ->
        if (imageUriArray.isNullOrEmpty()) {
            MultiStatusTip.showWarnTip(safeContext, "没有找到相关图片路径")
        } else {
            Single.just(true)
                .map {
                    return@map Luban.with(this).ignoreBy(100).load(imageUriArray).get()
                }
                .compose(RxSchedulers.applySingleSchedulers())
                .doOnSuccess {
                    onCompressResult.invoke(it)
                }.doOnError {
                    onCompressResult.invoke(null)
                }.subscribe()
        }
    }
}

fun Context?.fastCompressImagesByPath(
    imagePathArray: List<String>?,
    onCompressResult: (List<File>?) -> Unit
) {
    this.checkSafeContext { safeContext ->
        if (imagePathArray.isNullOrEmpty()) {
            MultiStatusTip.showWarnTip(safeContext, "没有找到相关图片路径")
        } else {
            Single.just(true)
                .map {
                    return@map Luban.with(this).ignoreBy(100).load(imagePathArray).get()
                }
                .compose(RxSchedulers.applySingleSchedulers())
                .doOnSuccess {
                    onCompressResult.invoke(it)
                }.doOnError {
                    onCompressResult.invoke(null)
                }.subscribe()
        }
    }
}

fun Context?.downloadImage(
    imageUrl: String?,
    onLoadImageProgressListener: OnLoadImageProgressListener
) {
    ProgressGlide(this, imageUrl, onLoadImageProgressListener)
}


fun Context?.showImageToBitmap(imageUrl: String?, onBitmapListener: (Bitmap) -> Unit) {
    this.checkSafeContext { safeContext ->
        GlideApp.with(safeContext).asBitmap().load(imageUrl)
            .transition(BitmapTransitionOptions.withCrossFade())
            .into(object : CustomTarget<Bitmap>() {
                override fun onLoadCleared(placeholder: Drawable?) {
                }

                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    onBitmapListener.invoke(resource)
                }
            })
    }

}