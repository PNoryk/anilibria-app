package tv.anilibria.core.types

data class Count(val value: Long)

fun Long.asCount() = Count(this)
