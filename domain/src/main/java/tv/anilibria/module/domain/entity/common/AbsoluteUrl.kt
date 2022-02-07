package tv.anilibria.module.domain.entity.common

data class AbsoluteUrl(val value: String)

fun String.asAbsoluteUrl() = AbsoluteUrl(this)