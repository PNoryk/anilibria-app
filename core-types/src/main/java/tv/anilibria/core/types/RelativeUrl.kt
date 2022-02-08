package tv.anilibria.core.types

data class RelativeUrl(val url: String)

fun String.asRelativeUrl() = RelativeUrl(this)