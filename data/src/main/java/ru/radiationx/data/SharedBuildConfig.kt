package ru.radiationx.data

@Deprecated("old data")
interface SharedBuildConfig {
    val applicationId: String
    val versionName: String
    val versionCode: Int
    val debug: Boolean
}