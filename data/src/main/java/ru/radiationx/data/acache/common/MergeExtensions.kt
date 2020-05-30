package ru.radiationx.data.acache.common


fun <T> T.mergeField(new: T): T {
    return if (this != new || (this == null && new != null)) new else this
}