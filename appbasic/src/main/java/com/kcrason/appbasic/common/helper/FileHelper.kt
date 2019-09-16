package com.kcrason.appbasic.common.helper

import android.content.Context
import android.graphics.Bitmap
import android.os.Environment
import java.lang.Exception
import android.media.MediaScannerConnection
import java.io.*
import kotlin.math.abs


/**
 * @author KCrason
 * @date 2019/5/20 15:44
 * @description
 */

object FileHelper {

    private fun generateDefaultPath(context: Context): String? {
        return if (Environment.MEDIA_MOUNTED == Environment.getExternalStorageState()) {
            Environment.getExternalStorageDirectory()?.absolutePath + File.separator + "AAssistant"
        } else {
            context.filesDir?.absolutePath + File.separator + "AAssistant"
        }
    }


    private fun generateSavePictureFile(context: Context): File {
        val pictureFile = File(generateDefaultPath(context) + File.separator + "cachePicture")
        if (!pictureFile.exists()) {
            pictureFile.mkdirs()
        }
        return pictureFile
    }

    private fun generateDownloadFile(context: Context): File {
        val pictureFile = File(generateDefaultPath(context) + File.separator + "download")
        if (!pictureFile.exists()) {
            pictureFile.mkdirs()
        }
        return pictureFile
    }


    fun generateCropPictureFile(context: Context): File {
        val pictureFile = File(generateDefaultPath(context) + File.separator + "cropPicture")
        if (!pictureFile.exists()) {
            pictureFile.mkdirs()
        }
        return File(pictureFile, "${System.currentTimeMillis()}.png")
    }

    fun generateTakePictureFile(context: Context): File {
        val pictureFile = File(generateDefaultPath(context) + File.separator + "takePicture")
        if (!pictureFile.exists()) {
            pictureFile.mkdirs()
        }
        return File(pictureFile.absolutePath, "${System.currentTimeMillis()}.png")
    }

    fun saveDownloadFile(context: Context, inputStream: InputStream, fileName: String): File? {
        val picFile = File(generateDownloadFile(context), fileName)
        if (!picFile.exists()) {
            picFile.createNewFile()
        }
        val fos: FileOutputStream?
        try {
            fos = FileOutputStream(picFile)
            val buff = ByteArray(1024)
            var byteCount = inputStream.read(buff)
            while (byteCount != -1) {
                fos.write(buff, 0, byteCount)
                byteCount = inputStream.read(buff)
            }
            fos.flush()
            fos.close()
            inputStream.close()
            return picFile
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return null
    }


    fun saveBitmap(context: Context, bitmap: Bitmap, url: String): String? {
        try {
            val picFile = File(generateSavePictureFile(context), "${abs(url.hashCode())}.jpg")
            if (!picFile.exists()) {
                picFile.createNewFile()
            }
            val fileOutputStream = FileOutputStream(picFile)
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream)
            fileOutputStream.flush()
            fileOutputStream.close()
            //保存完成之后扫描至相册显示。
            MediaScannerConnection.scanFile(
                context,
                arrayOf(picFile.absolutePath),
                arrayOf("image/*"),
                null
            )
            return picFile.absolutePath
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }
}