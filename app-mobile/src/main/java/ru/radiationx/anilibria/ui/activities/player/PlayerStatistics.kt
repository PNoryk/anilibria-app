package ru.radiationx.anilibria.ui.activities.player

import android.net.Uri
import ru.radiationx.data.analytics.AnalyticsErrorReporter
import ru.radiationx.data.analytics.ErrorReporterConstants
import ru.radiationx.data.analytics.TimeCounter
import ru.radiationx.data.analytics.features.PlayerAnalytics
import ru.radiationx.data.analytics.features.mapper.toAnalyticsQuality
import ru.radiationx.data.analytics.features.model.AnalyticsQuality

class PlayerStatistics(
    private val playerAnalytics: PlayerAnalytics,
    private val errorReporter: AnalyticsErrorReporter,
    private val qualityProvider: () -> PlayerQuality
) {

    private val timeToStartCounter = TimeCounter()

    private val loadingStatistics =
        mutableMapOf<String, MutableList<Pair<AnalyticsQuality, Long>>>()

    private fun getStatisticByDomain(host: String): MutableList<Pair<AnalyticsQuality, Long>> {
        if (!loadingStatistics.contains(host)) {
            loadingStatistics[host] = mutableListOf()
        }
        return loadingStatistics.getValue(host)
    }

    private fun putStatistics(uri: Uri, quality: AnalyticsQuality, time: Long) {
        uri.host?.let { getStatisticByDomain(it) }?.add(quality to time)
    }

    private fun getAverageStatisticsValues(): Map<String, Map<AnalyticsQuality, Long>> {
        return loadingStatistics
            .mapValues { statsMap ->
                statsMap.value
                    .groupBy { it.first }
                    .mapValues { qualityMap ->
                        qualityMap.value.map { it.second }.average().toLong()
                    }
            }
    }

    private val currentQuality: PlayerQuality
        get() = qualityProvider.invoke()

    val analyticsListener = PlayerAnalyticsListener(
        onCanceled = {
            putStatistics(
                it.uri,
                currentQuality.toPrefQuality().toAnalyticsQuality(),
                it.loadDurationMs
            )
        },
        onComplete = {
            putStatistics(
                it.uri,
                currentQuality.toPrefQuality().toAnalyticsQuality(),
                it.loadDurationMs
            )
        },
        onFirstFrame = {
            playerAnalytics.timeToStart(
                it?.host.toString(),
                currentQuality.toPrefQuality().toAnalyticsQuality(),
                timeToStartCounter.elapsed()
            )
        },
        onLoadError = {
            errorReporter.report(ErrorReporterConstants.group_player, "onLoadError", it)
            playerAnalytics.error(it)
        },
        onPlayerError = {
            errorReporter.report(
                ErrorReporterConstants.group_player,
                "onPlayerError",
                it
            )
            playerAnalytics.error(it)
        },
    )

    fun onCreate() {
        timeToStartCounter.start()
    }

    fun onDestroy() {
        getAverageStatisticsValues().forEach { statsEntry ->
            statsEntry.value.forEach { qualityEntry ->
                playerAnalytics.loadTime(statsEntry.key, qualityEntry.key, qualityEntry.value)
            }
        }
    }
}