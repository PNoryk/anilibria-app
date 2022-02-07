package tv.anilibria.module.domain.entity.common

data class Bytes(val value: Long)

fun Int.asBytes() = Bytes(this.toLong())

fun Long.asBytes() = Bytes(this)