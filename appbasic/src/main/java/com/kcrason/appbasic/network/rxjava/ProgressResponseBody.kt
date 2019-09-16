package com.kcrason.appbasic.network.rxjava

import okhttp3.MediaType
import okhttp3.ResponseBody
import okio.*


/**
 * @author KCrason
 * @date 2019/7/1 9:55
 * @description
 */
class ProgressResponseBody(
    private val responseBody: ResponseBody,
    onProgressListener: ((progress: Int, completeState: Boolean) -> Unit)
) : ResponseBody() {

    private val progressListener: ((progress: Int, completeState: Boolean) -> Unit) = onProgressListener
    private var bufferedSource: BufferedSource? = null

    override fun contentLength(): Long {
        return responseBody.contentLength()
    }

    override fun contentType(): MediaType? {
        return responseBody.contentType()
    }

    override fun source(): BufferedSource {
        if (bufferedSource == null) {
            bufferedSource = Okio.buffer(source(responseBody.source()))
        }
        return bufferedSource!!
    }


    private fun source(source: Source): Source {
        return object : ForwardingSource(source) {
            var totalBytesRead = 0L
            override fun read(sink: Buffer, byteCount: Long): Long {
                val bytesRead = super.read(sink, byteCount)
                if (bytesRead != -1L) {
                    totalBytesRead += bytesRead
                }
                progressListener.invoke((totalBytesRead * 100 / responseBody.contentLength()).toInt(), bytesRead == -1L)
                return bytesRead
            }
        }
    }
}