package ru.radiationx.shared_app.common

import android.content.Context
import com.nostra13.universalimageloader.core.assist.ContentLengthInputStream
import com.nostra13.universalimageloader.core.download.BaseImageDownloader
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.InputStream
import javax.inject.Inject
import javax.inject.Provider

class OkHttpImageDownloader @Inject constructor(
    private val context: Context,
    private val okHttpProvider: Provider<OkHttpClient>
) : BaseImageDownloader(context) {

    override fun getStreamFromNetwork(imageUri: String, extra: Any?): InputStream {
        if (!imageUri.contains("static.anilibria.tv")) {
            return super.getStreamFromNetwork(imageUri, extra)
        }
        val request = Request.Builder().url(imageUri).build()
        val response = okHttpProvider.get().newCall(request).execute()
        val responseBody = response.body()
        val inputStream = responseBody!!.byteStream()
        val contentLength = responseBody.contentLength().toInt()
        return ContentLengthInputStream(inputStream, contentLength)
    }
}