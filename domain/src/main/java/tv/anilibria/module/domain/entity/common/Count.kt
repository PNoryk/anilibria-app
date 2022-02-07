package tv.anilibria.module.domain.entity.common

data class Count(val value: Long)

fun Long.asCount() = Count(this)
