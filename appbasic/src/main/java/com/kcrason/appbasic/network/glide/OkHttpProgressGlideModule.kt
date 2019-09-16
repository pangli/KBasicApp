package com.kcrason.appbasic.network.glide

import android.content.Context
import com.bumptech.glide.Glide
import com.bumptech.glide.Registry
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.module.AppGlideModule
import com.bumptech.glide.load.model.GlideUrl
import com.kcrason.appbasic.common.RxBus
import com.kcrason.appbasic.mvp.model.event.ImageProgressEvent
import com.kcrason.appbasic.network.rxjava.ProgressResponseBody
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import java.io.InputStream


/**
 * @author KCrason
 * @date 2019/8/29 14:27
 * @description
 */
@GlideModule
class OkHttpProgressGlideModule : AppGlideModule() {

    override fun registerComponents(context: Context, glide: Glide, registry: Registry) {
        super.registerComponents(context, glide, registry)
        val client = OkHttpClient.Builder()
            .addNetworkInterceptor(ImageProgressInterceptor()).build()
        registry.replace(
            GlideUrl::class.java,
            InputStream::class.java,
            OkHttpUrlLoader.Factory(client)
        )
    }

    class ImageProgressInterceptor : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            val originalResponse = chain.proceed(chain.request())
            return originalResponse.newBuilder()
                .body(originalResponse.body()?.let {
                    ProgressResponseBody(it) { progress, completeState ->
                        RxBus.publish(ImageProgressEvent(progress, completeState))
                    }
                }).build()
        }
    }
}