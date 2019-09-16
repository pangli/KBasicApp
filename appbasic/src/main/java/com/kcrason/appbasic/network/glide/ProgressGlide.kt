package com.kcrason.appbasic.network.glide

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.kcrason.appbasic.common.RxBus
import com.kcrason.appbasic.common.addDisposable
import com.kcrason.appbasic.listeners.OnLoadImageProgressListener
import com.kcrason.appbasic.mvp.model.event.ImageProgressEvent
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * @author KCrason
 * @date 2019/8/29 15:04
 * @description
 */
class ProgressGlide(
    context: Context?,
    imageUrl: String?,
    onLoadImageProgressListener: OnLoadImageProgressListener
) {
    init {
        if (context != null) {
            context.addDisposable(
                RxBus.listen(ImageProgressEvent::class.java).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe {
                        onLoadImageProgressListener.onProgress(it.progress, it.completeState)
                    }
            )
            GlideApp.with(context)
                .asBitmap().diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .load(imageUrl)
                .into(ImageProgressTarget(onLoadImageProgressListener))
        }
    }


    class ImageProgressTarget(
        private val onLoadImageProgressListener: OnLoadImageProgressListener
    ) :
        CustomTarget<Bitmap>() {
        override fun onLoadStarted(placeholder: Drawable?) {
            super.onLoadStarted(placeholder)
            onLoadImageProgressListener.onLoadStarted()
        }

        override fun onLoadFailed(errorDrawable: Drawable?) {
            super.onLoadFailed(errorDrawable)
            onLoadImageProgressListener.onLoadFailed()
        }

        override fun onLoadCleared(placeholder: Drawable?) {
        }

        override fun onResourceReady(
            resource: Bitmap,
            transition: Transition<in Bitmap>?
        ) {
            onLoadImageProgressListener.onResourceReady(resource)
        }
    }
}