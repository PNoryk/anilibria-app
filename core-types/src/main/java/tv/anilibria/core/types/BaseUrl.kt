package tv.anilibria.core.types

data class BaseUrl(val value: String)

fun String.asBaseUrl() = BaseUrl(this)