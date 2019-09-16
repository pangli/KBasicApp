package com.kcrason.appbasic.common

import android.Manifest
import android.net.Uri
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.kcrason.appbasic.R
import com.kcrason.appbasic.common.helper.FileHelper
import com.yalantis.ucrop.UCrop

/**
 * @author KCrason
 * @date 2019/8/28 15:25
 * @description
 */

fun Fragment?.cropPicture(
    pictureUri: Uri
) {
    this?.activity?.let { activity ->
        activity.fastPermissions(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE)) { granted ->
            if (granted) {
                val cropOptions = UCrop.Options()
                cropOptions.setHideBottomControls(true)
                cropOptions.setFreeStyleCropEnabled(true)
                cropOptions.withAspectRatio(1f, 1f)
                cropOptions.setToolbarColor(ContextCompat.getColor(activity, R.color.color_3a445f))
                cropOptions.setStatusBarColor(
                    ContextCompat.getColor(
                        activity,
                        R.color.color_3a445f
                    )
                )
                cropOptions.setActiveWidgetColor(
                    ContextCompat.getColor(
                        activity,
                        R.color.color_000000
                    )
                )
                cropOptions.setToolbarWidgetColor(
                    ContextCompat.getColor(
                        activity,
                        R.color.color_ffffff
                    )
                )
                cropOptions.setRootViewBackgroundColor(
                    ContextCompat.getColor(
                        activity,
                        R.color.color_000000
                    )
                )
                val uCrop =
                    UCrop.of(
                        pictureUri,
                        activity.fileToUri(FileHelper.generateCropPictureFile(activity))
                    )
                uCrop.withOptions(cropOptions)
                uCrop.start(activity, this, UCrop.REQUEST_CROP)
            }
        }
    }
}