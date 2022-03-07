package tv.anilibria.feature.analytics.impl.events

import android.util.Log
import toothpick.InjectConstructor
import tv.anilibria.plugin.data.analytics.AnalyticsSender

@InjectConstructor
class LoggingAnalyticsSender : AnalyticsSender {

    override fun send(key: String, vararg params: Pair<String, String>) {
        Log.d("AnalyticsSender", "key: $key, params: ${params.toMap()}")
    }
}