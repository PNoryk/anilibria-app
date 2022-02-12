package tv.anilibria.plugin.data.analytics

interface AnalyticsSender {

    fun send(key: String, vararg params: Pair<String, String>)
}