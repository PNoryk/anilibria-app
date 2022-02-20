package ru.radiationx.shared_app.analytics.profile

import toothpick.InjectConstructor

@InjectConstructor
class CombinedAnalyticsProfile(
    private val appMetrica: AppMetricaAnalyticsProfile,
    private val logging: LoggingAnalyticsProfile
) : AnalyticsProfile {

    override fun update() {
        logging.update()
        appMetrica.update()
    }
}