package ru.radiationx.shared_app.analytics.errors

import com.yandex.metrica.YandexMetrica
import toothpick.InjectConstructor
import tv.anilibria.plugin.data.analytics.AnalyticsErrorReporter

@InjectConstructor
class AppMetricaErrorReporter : AnalyticsErrorReporter {

    override fun report(message: String, error: Throwable) {
        safeReport {
            YandexMetrica.reportError(message, error)
        }
    }

    override fun report(group: String, message: String) {
        safeReport {
            YandexMetrica.reportError(group, message)
        }
    }

    override fun report(group: String, message: String, error: Throwable) {
        safeReport {
            YandexMetrica.reportError(group, message, error)
        }
    }

    private fun safeReport(block: () -> Unit) {
        try {
            block()
        } catch (ex: Throwable) {
            ex.printStackTrace()
        }
    }
}