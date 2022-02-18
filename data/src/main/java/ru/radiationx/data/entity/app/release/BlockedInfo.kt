package ru.radiationx.data.entity.app.release

import java.io.Serializable

@Deprecated("old data")
class BlockedInfo : Serializable {
    var isBlocked: Boolean = false
    var reason: String? = null
}