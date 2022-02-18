package ru.radiationx.data.analytics

@Deprecated("old data")
interface AnalyticsSender {

    fun send(key: String, vararg params: Pair<String, String>)
}