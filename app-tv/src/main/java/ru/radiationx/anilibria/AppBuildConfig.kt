package ru.radiationx.anilibria

import toothpick.InjectConstructor
import tv.anilibria.plugin.shared.appinfo.SharedBuildConfig

@InjectConstructor
class AppBuildConfig : SharedBuildConfig {

    override val applicationId: String = BuildConfig.APPLICATION_ID

    override val versionName: String = BuildConfig.VERSION_NAME

    override val versionCode: Int = BuildConfig.VERSION_CODE

    override val debug: Boolean = BuildConfig.DEBUG

    override val buildDate: String = ""
}