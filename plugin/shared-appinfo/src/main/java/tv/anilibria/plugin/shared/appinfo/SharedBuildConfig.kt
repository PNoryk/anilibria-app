package tv.anilibria.plugin.shared.appinfo

interface SharedBuildConfig {
    val applicationId: String
    val versionName: String
    val versionCode: Int
    val debug: Boolean
}