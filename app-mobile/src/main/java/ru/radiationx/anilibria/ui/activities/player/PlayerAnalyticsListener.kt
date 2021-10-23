package ru.radiationx.anilibria.ui.activities.player

import android.net.Uri
import android.view.Surface
import com.google.android.exoplayer2.ExoPlaybackException
import com.google.android.exoplayer2.analytics.AnalyticsListener
import com.google.android.exoplayer2.source.MediaSourceEventListener
import java.io.IOException

class PlayerAnalyticsListener(
    private val onCanceled: (MediaSourceEventListener.LoadEventInfo) -> Unit,
    private val onComplete: (MediaSourceEventListener.LoadEventInfo) -> Unit,
    private val onFirstFrame: (Uri?) -> Unit,
    private val onLoadError: (IOException) -> Unit,
    private val onPlayerError: (ExoPlaybackException) -> Unit,
) : AnalyticsListener {

    private var wasFirstFrame = false
    private var lastLoadedUri: Uri? = null

    private var lastLoadError: Throwable? = null
    private var lastPlayerError: Throwable? = null

    override fun onLoadCanceled(
        eventTime: AnalyticsListener.EventTime,
        loadEventInfo: MediaSourceEventListener.LoadEventInfo,
        mediaLoadData: MediaSourceEventListener.MediaLoadData
    ) {
        super.onLoadCanceled(eventTime, loadEventInfo, mediaLoadData)
        lastLoadedUri = loadEventInfo.uri
        onCanceled.invoke(loadEventInfo)
    }

    override fun onLoadCompleted(
        eventTime: AnalyticsListener.EventTime,
        loadEventInfo: MediaSourceEventListener.LoadEventInfo,
        mediaLoadData: MediaSourceEventListener.MediaLoadData
    ) {
        super.onLoadCompleted(eventTime, loadEventInfo, mediaLoadData)
        lastLoadedUri = loadEventInfo.uri
        onComplete.invoke(loadEventInfo)
    }


    override fun onRenderedFirstFrame(
        eventTime: AnalyticsListener.EventTime,
        surface: Surface?
    ) {
        super.onRenderedFirstFrame(eventTime, surface)
        if (!wasFirstFrame) {
            onFirstFrame.invoke(lastLoadedUri)
            wasFirstFrame = true
        }
    }

    override fun onLoadError(
        eventTime: AnalyticsListener.EventTime,
        loadEventInfo: MediaSourceEventListener.LoadEventInfo,
        mediaLoadData: MediaSourceEventListener.MediaLoadData,
        error: IOException,
        wasCanceled: Boolean
    ) {
        super.onLoadError(eventTime, loadEventInfo, mediaLoadData, error, wasCanceled)
        if (lastLoadError?.toString() != error.toString()) {
            onLoadError.invoke(error)
            lastLoadError = error
        }
    }

    override fun onPlayerError(
        eventTime: AnalyticsListener.EventTime,
        error: ExoPlaybackException
    ) {
        super.onPlayerError(eventTime, error)
        if (lastPlayerError?.toString() != error.toString()) {
            onPlayerError.invoke(error)
            lastLoadError = error
        }
    }
}