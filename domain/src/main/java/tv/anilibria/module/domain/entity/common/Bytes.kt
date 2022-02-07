package tv.anilibria.module.domain.entity.common

data class Bytes(val value: Long)

fun Long.asBytes() = Bytes(this)