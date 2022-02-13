package tv.anilibria.feature.downloads.data.shared

import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.util.Log
import android.webkit.URLUtil
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import toothpick.InjectConstructor

@InjectConstructor
class DownloadControllerImpl(
    private val context: Context,
    private val dataSource: DownloadsDataSource,
) : DownloadController, LifecycleObserver {

    private val downloadManager by lazy { context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager }

    init {
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun start() {
        Log.e("DownloadController", "start")
        dataSource.enableObserving(true)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun stop() {
        Log.e("DownloadController", "stop")
        dataSource.enableObserving(false)
    }

    override fun startDownload(url: String) {
        Log.e("DownloadController", "startDownload $url")
        val downloadId = enqueueDownload(url, null)
        dataSource.notifyDownloadStart(downloadId)
    }

    override fun removeDownload(url: String) {
        Log.e("DownloadController", "removeDownload $url")
        getDownload(url)?.also {
            downloadManager.remove(it.downloadId)
            dataSource.notifyDownloadRemove(it.downloadId)
        }
    }

    override fun getDownload(url: String): DownloadItem? =
        dataSource.getDownloads().firstOrNull { it.url == url }

    override fun observeDownload(url: String): Flow<DownloadItem> = dataSource
        .observeDownload()
        .filter { it.url == url }

    override fun observeCompleted(url: String): Flow<DownloadItem> = dataSource
        .observeCompleted()
        .filter { it.url == url }

    private fun enqueueDownload(url: String, name: String?): Long {
        val fileName = name ?: URLUtil.guessFileName(url, null, null)
        val request = DownloadManager.Request(Uri.parse(url)).apply {
            setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE)
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
                setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName)
            }
            setTitle(fileName)
            setDescription(fileName)
        }
        return downloadManager.enqueue(request)
    }
}