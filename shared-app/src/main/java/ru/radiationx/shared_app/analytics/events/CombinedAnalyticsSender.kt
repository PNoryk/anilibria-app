package ru.radiationx.shared_app.analytics.events

import toothpick.InjectConstructor
import tv.anilibria.plugin.data.analytics.AnalyticsSender

@InjectConstructor
class CombinedAnalyticsSender(
    private val appMetrica: AppMetricaAnalyticsSender,
    private val logging: LoggingAnalyticsSender
) : AnalyticsSender {

    override fun send(key: String, vararg params: Pair<String, String>) {
        logging.send(key, *params)
        appMetrica.send(key, *params)
    }
}