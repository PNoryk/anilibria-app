package tv.anilibria.core.types

data class Bytes(val value: Long)

fun Long.asBytes() = Bytes(this)