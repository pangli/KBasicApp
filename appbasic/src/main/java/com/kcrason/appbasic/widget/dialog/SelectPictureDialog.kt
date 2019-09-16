package com.kcrason.appbasic.widget.dialog

import android.Manifest
import android.app.Activity
import android.content.Context
import android.view.Gravity
import android.widget.LinearLayout
import com.kcrason.appbasic.widget.dialog.base.CommonStyleDialog
import kotlinx.android.synthetic.main.dialog_select_picture_view.view.*
import android.provider.MediaStore
import android.content.Intent
import android.net.Uri
import android.os.Build
import com.kcrason.appbasic.R
import com.kcrason.appbasic.common.*
import com.kcrason.appbasic.common.helper.FileHelper
import com.kcrason.appbasic.common.helper.Values

class SelectPictureDialog(context: Context) :
    CommonStyleDialog(context, R.layout.dialog_select_picture_view) {

    private var mMaxCount: Int = 1

    private var supportCrop: Boolean = false

    private var mSelectPictureListener: ((imagePathResult: List<String>?, imageUriResult: List<Uri>?) -> Unit)? =
        null

    override fun dialogWidth(): Int {
        return LinearLayout.LayoutParams.MATCH_PARENT
    }

    fun setOnSelectedPictureListener(selectPictureListener: ((imagePathResult: List<String>?, imageUriResult: List<Uri>?) -> Unit)?): SelectPictureDialog {
        this.mSelectPictureListener = selectPictureListener
        return this
    }

    fun openCrop(openCrop: Boolean): SelectPictureDialog {
        this.supportCrop = openCrop
        return this
    }

    fun setMaxCount(maxCount: Int): SelectPictureDialog {
        this.mMaxCount = maxCount
        return this
    }

    override fun initDialog() {
        setWindowAnimations(R.style.ShareDialogAnimation)
        setDialogGravity(Gravity.BOTTOM)
        containerView.txtTakePhoto.setOnClickListener {
            dismiss()
            mContext.fastPermissions(
                arrayOf(
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.CAMERA
                )
            ) { granted ->
                if (granted) {
                    dismiss()
                    takePhoto()
                }
            }
        }
        containerView.txtGallery.setOnClickListener {
            dismiss()
            mContext.fastSelectImages(mMaxCount) { imagePathResult, imageUriResult ->
                if (!imageUriResult.isNullOrEmpty() && imageUriResult.size == 1 && supportCrop) {
                    mContext.fastCropPicture(imageUriResult[0]) { cropResult ->
                        cropResult?.let {
                            mSelectPictureListener?.invoke(
                                listOfNotNull(it.path),
                                arrayListOf(it)
                            )
                        }
                    }
                } else {
                    mSelectPictureListener?.invoke(imagePathResult, imageUriResult)
                }
            }
        }
        containerView.txtCancel.setOnClickListener { dismiss() }
    }


    private fun takePhoto() {
        val photoFile = FileHelper.generateTakePictureFile(mContext)
        val uri = mContext.fileToUri(photoFile)
        val intent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                .putExtra(MediaStore.EXTRA_OUTPUT, uri)
                .addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
        } else {
            Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                .putExtra(MediaStore.EXTRA_OUTPUT, uri)
        }
        mContext.fastActivityForResult(
            intent,
            Values.RequestCode.REQUEST_TAKE_PHOTO
        ) { requestCode, resultCode, _ ->
            if (requestCode == Values.RequestCode.REQUEST_TAKE_PHOTO
                && resultCode == Activity.RESULT_OK
            ) {
                if (supportCrop) {
                    mContext.fastCropPicture(uri) { cropResult ->
                        cropResult?.let {
                            mSelectPictureListener?.invoke(
                                listOfNotNull(it.path),
                                arrayListOf(it)
                            )
                        }
                    }
                } else {
                    mSelectPictureListener?.invoke(
                        arrayListOf(photoFile.absolutePath),
                        arrayListOf(uri)
                    )
                }
            }
        }
    }
}