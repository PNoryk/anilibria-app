package tv.anilibria.feature.analytics.impl.profile

import android.util.Log
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import toothpick.InjectConstructor
import tv.anilibria.feature.analytics.impl.AnalyticsProfileDataSource
import tv.anilibria.plugin.data.analytics.profile.AnalyticsProfile

@InjectConstructor
class LoggingAnalyticsProfile(
    private val dataSource: AnalyticsProfileDataSource
) : AnalyticsProfile {

    override fun update() {
        GlobalScope.launch {
            try {
                unsafeUpdate()
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
        }
    }

    private suspend fun unsafeUpdate() {
        val attributes = dataSource.getAttributes()
        Log.d("LoggingAnalyticsProfile", attributes.toString())
    }

    private fun Any.mapToAttr(name: String): Pair<String, Any> = Pair(name, this)
}