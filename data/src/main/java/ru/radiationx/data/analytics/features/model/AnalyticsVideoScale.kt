package ru.radiationx.data.analytics.features.model

@Deprecated("old data")
enum class AnalyticsVideoScale(val value: String) {
    CENTER("center"),
    CENTER_CROP("center_crop"),
    CENTER_INSIDE("center_inside"),
    FIT_CENTER("fit_center"),
    FIT_XY("fit_xy"),
    NONE("none"),
    UNKNOWN("unknown")
}