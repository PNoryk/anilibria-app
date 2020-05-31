package anilibria.tv.api.impl.common

import java.util.*

fun Long.dateFromSec() = Date(this * 1000)