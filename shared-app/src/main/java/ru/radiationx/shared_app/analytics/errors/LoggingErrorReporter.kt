package ru.radiationx.shared_app.analytics.errors

import android.util.Log
import toothpick.InjectConstructor
import tv.anilibria.plugin.data.analytics.AnalyticsErrorReporter

@InjectConstructor
class LoggingErrorReporter : AnalyticsErrorReporter {

    override fun report(message: String, error: Throwable) {
        Log.e("LoggingErrorReporter", message, error)
    }

    override fun report(group: String, message: String) {
        Log.e("LoggingErrorReporter", "$group -> $message")
    }

    override fun report(group: String, message: String, error: Throwable) {
        Log.e("LoggingErrorReporter", "$group -> $message", error)
    }
}