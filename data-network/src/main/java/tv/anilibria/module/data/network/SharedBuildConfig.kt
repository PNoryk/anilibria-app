package tv.anilibria.module.data.network

interface SharedBuildConfig {
    val applicationId: String
    val versionName: String
    val versionCode: Int
    val debug: Boolean
}