package tv.anilibria.module.domain.entity.common

data class BaseUrl(val value: String)

fun String.asBaseUrl() = BaseUrl(this)