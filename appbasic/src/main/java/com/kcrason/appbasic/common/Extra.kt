package com.kcrason.appbasic.common

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.ActivityInfo
import android.net.Uri
import android.os.Build
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.kcrason.appbasic.R
import com.kcrason.appbasic.common.helper.FileHelper
import com.kcrason.appbasic.common.helper.Values
import com.kcrason.appbasic.common.loader.Glide4Engine
import com.yalantis.ucrop.UCrop
import com.zhihu.matisse.Matisse
import com.zhihu.matisse.MimeType
import com.zhihu.matisse.internal.entity.CaptureStrategy

object Extra {

    fun innerSelectImages(context: Context?, maxSelectable: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            context.fastPermissions(
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
            ) { granted ->
                if (granted) {
                    initMatisse(context, maxSelectable)
                }
            }
        } else {
            initMatisse(context, maxSelectable)
        }
    }

    private fun initMatisse(context: Context?, maxSelectable: Int) {
        if (context is Fragment) {
            Matisse.from(context)
                .choose(MimeType.ofImage())
                .showSingleMediaType(true)
                .countable(true)
                .maxSelectable(maxSelectable)
                .theme(R.style.Matisse_Custom)
                .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                .capture(false)
                .captureStrategy(CaptureStrategy(true, "com.fid.basicmodule.fileprovider"))
                .thumbnailScale(0.85f)
                .imageEngine(Glide4Engine())
                .forResult(Values.RequestCode.REQUEST_MATISSE)
        } else if (context is Activity) {
            Matisse.from(context)
                .choose(MimeType.ofImage())
                .showSingleMediaType(true)
                .countable(true)
                .maxSelectable(maxSelectable)
                .theme(R.style.Matisse_Custom)
                .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                .capture(false)
                .captureStrategy(CaptureStrategy(true, "com.fid.basicmodule.fileprovider"))
                .thumbnailScale(0.85f)
                .imageEngine(Glide4Engine())
                .forResult(Values.RequestCode.REQUEST_MATISSE)
        }
    }


    fun cropPicture(
        context: Context?,
        pictureUri: Uri
    ) {
        context.checkSafeContext { safeContext ->
            safeContext.fastPermissions(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE)) { granted ->
                if (granted) {
                    val cropOptions = UCrop.Options()
                    cropOptions.setHideBottomControls(true)
                    cropOptions.setFreeStyleCropEnabled(true)
                    cropOptions.withAspectRatio(1f, 1f)
                    cropOptions.setToolbarColor(
                        ContextCompat.getColor(
                            safeContext,
                            R.color.color_3a445f
                        )
                    )
                    cropOptions.setStatusBarColor(
                        ContextCompat.getColor(
                            safeContext,
                            R.color.color_3a445f
                        )
                    )
                    cropOptions.setActiveWidgetColor(
                        ContextCompat.getColor(
                            safeContext,
                            R.color.color_000000
                        )
                    )
                    cropOptions.setToolbarWidgetColor(
                        ContextCompat.getColor(
                            safeContext,
                            R.color.color_ffffff
                        )
                    )
                    cropOptions.setRootViewBackgroundColor(
                        ContextCompat.getColor(
                            safeContext,
                            R.color.color_000000
                        )
                    )
                    val uCrop =
                        UCrop.of(
                            pictureUri,
                            Uri.fromFile(FileHelper.generateCropPictureFile(safeContext))
                        )
                    uCrop.withOptions(cropOptions)
                    if (safeContext is Fragment) {
                        uCrop.start(safeContext, safeContext)
                    } else if (safeContext is Activity) {
                        uCrop.start(safeContext)
                    }
                }
            }
        }
    }

}