package tv.anilibria.core.types

data class AbsoluteUrl(val value: String)

fun String.asAbsoluteUrl() = AbsoluteUrl(this)