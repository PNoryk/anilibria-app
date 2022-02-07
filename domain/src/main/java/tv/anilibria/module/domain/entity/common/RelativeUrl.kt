package tv.anilibria.module.domain.entity.common

data class RelativeUrl(val url: String)

fun String.asRelativeUrl() = RelativeUrl(this)