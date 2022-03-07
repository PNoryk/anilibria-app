package ru.radiationx.anilibria.ui.fragments.settings

import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.preference.Preference
import androidx.preference.SwitchPreferenceCompat
import ru.radiationx.anilibria.R
import ru.radiationx.anilibria.extension.getCompatDrawable
import ru.radiationx.anilibria.navigation.Screens
import ru.radiationx.anilibria.presentation.common.IErrorHandler
import ru.radiationx.shared_app.AppLinkHelper
import ru.radiationx.shared_app.di.injectDependencies
import tv.anilibria.core.types.AbsoluteUrl
import tv.anilibria.feature.player.data.PlayerPreferencesStorage
import tv.anilibria.feature.player.data.prefs.PrefferedPlayerQuality
import tv.anilibria.feature.player.data.prefs.PrefferedPlayerType
import tv.anilibria.feature.analytics.AnalyticsConstants
import tv.anilibria.feature.analytics.features.SettingsAnalytics
import tv.anilibria.feature.analytics.features.mapper.toAnalyticsPlayer
import tv.anilibria.feature.analytics.features.mapper.toAnalyticsQuality
import tv.anilibria.feature.analytics.features.model.AnalyticsAppTheme
import tv.anilibria.app.mobile.preferences.PreferencesStorage
import tv.anilibria.plugin.shared.appinfo.SharedBuildConfig
import javax.inject.Inject

/**
 * Created by radiationx on 25.12.16.
 */

class SettingsFragment : BaseSettingFragment() {

    @Inject
    lateinit var preferencesStorage: PreferencesStorage

    @Inject
    lateinit var playerPreferencesStorage: PlayerPreferencesStorage

    @Inject
    lateinit var errorHandler: IErrorHandler

    @Inject
    lateinit var settingsAnalytics: SettingsAnalytics

    @Inject
    lateinit var sharedBuildConfig: SharedBuildConfig

    @Inject
    lateinit var appLinkHelper: AppLinkHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        injectDependencies()
        super.onCreate(savedInstanceState)
        addPreferencesFromResource(R.xml.preferences)

        findPreference<SwitchPreferenceCompat>("notifications.all")?.apply {
            setOnPreferenceChangeListener { preference, newValue ->
                (newValue as? Boolean)?.also(settingsAnalytics::notificationMainChange)
                return@setOnPreferenceChangeListener true
            }
        }

        findPreference<SwitchPreferenceCompat>("notifications.service")?.apply {
            setOnPreferenceChangeListener { preference, newValue ->
                (newValue as? Boolean)?.also(settingsAnalytics::notificationSystemChange)
                return@setOnPreferenceChangeListener true
            }
        }

        findPreference<SwitchPreferenceCompat>("app_theme_dark")?.apply {
            setOnPreferenceChangeListener { preference, newValue ->
                (newValue as? Boolean)?.also { isDark ->
                    val theme = if (isDark) {
                        AnalyticsAppTheme.DARK
                    } else {
                        AnalyticsAppTheme.LIGHT
                    }
                    settingsAnalytics.themeChange(theme)
                }
                return@setOnPreferenceChangeListener true
            }
        }

        findPreference<SwitchPreferenceCompat>("episodes_is_reverse")?.apply {
            setOnPreferenceChangeListener { preference, newValue ->
                (newValue as? Boolean)?.also(settingsAnalytics::episodesOrderChange)
                return@setOnPreferenceChangeListener true
            }
        }

        findPreference<Preference>("quality")?.apply {
            val savedQuality = playerPreferencesStorage.quality.blockingGet()
            icon = getQualityIcon(savedQuality)
            summary = getQualityTitle(savedQuality)
            setOnPreferenceClickListener { preference ->
                settingsAnalytics.qualityClick()
                val values = arrayOf(
                    PrefferedPlayerQuality.SD,
                    PrefferedPlayerQuality.HD,
                    PrefferedPlayerQuality.FULL_HD,
                    PrefferedPlayerQuality.NOT_SELECTED,
                    PrefferedPlayerQuality.ALWAYS_ASK,
                )
                val titles = values.map { getQualityTitle(it) }.toTypedArray()
                AlertDialog.Builder(preference.context)
                    .setTitle(preference.title)
                    .setItems(titles) { _, which ->
                        val quality = values[which]
                        settingsAnalytics.qualityChange(quality.toAnalyticsQuality())
                        playerPreferencesStorage.quality.blockingSet(quality)
                        icon = getQualityIcon(quality)
                        summary = getQualityTitle(quality)
                    }
                    .show()
                false
            }
        }

        findPreference<Preference>("player_type")?.apply {
            val savedPlayerType = playerPreferencesStorage.playerType.blockingGet()
            icon = this.context.getCompatDrawable(R.drawable.ic_play_circle_outline)
            summary = getPlayerTypeTitle(savedPlayerType)
            setOnPreferenceClickListener { preference ->
                settingsAnalytics.playerClick()
                val values = arrayOf(
                    PrefferedPlayerType.EXTERNAL,
                    PrefferedPlayerType.INTERNAL,
                    PrefferedPlayerType.NOT_SELECTED,
                    PrefferedPlayerType.ALWAYS_ASK
                )
                val titles = values.map { getPlayerTypeTitle(it) }.toTypedArray()
                AlertDialog.Builder(preference.context)
                    .setTitle(preference.title)
                    .setItems(titles) { dialog, which ->
                        val playerType = values[which]
                        settingsAnalytics.playerChange(playerType.toAnalyticsPlayer())
                        playerPreferencesStorage.playerType.blockingSet(playerType)
                        summary = getPlayerTypeTitle(playerType)
                    }
                    .show()
                false
            }
        }

        findPreference<Preference>("about.application")?.apply {
            summary = "Версия ${sharedBuildConfig.versionName}"
        }

        findPreference<Preference>("about.app_other_apps")?.apply {
            icon = this.context.getCompatDrawable(R.drawable.ic_anilibria)
            setOnPreferenceClickListener {
                settingsAnalytics.otherAppsClick()
                appLinkHelper.openLink(AbsoluteUrl("https://anilibria.app/"))
                false
            }
        }

        findPreference<Preference>("about.app_topic_4pda")?.apply {
            icon = this.context.getCompatDrawable(R.drawable.ic_4pda)
            setOnPreferenceClickListener {
                settingsAnalytics.fourPdaClick()
                appLinkHelper.openLink(AbsoluteUrl("http://4pda.ru/forum/index.php?showtopic=886616"))
                false
            }
        }

        /*findPreference("about.app_play_market")?.apply {
            icon = ContextCompat.getDrawable(this.context, R.drawable.ic_play_market)
            setOnPreferenceClickListener { preference ->
                Utils.externalLink("https://play.google.com/store/apps/details?id=ru.radiationx.anilibria")
                false
            }
        }*/

        findPreference<Preference>("about.check_update")?.apply {
            setOnPreferenceClickListener {
                settingsAnalytics.checkUpdatesClick()
                val intent = Screens.AppUpdateScreen(true, AnalyticsConstants.screen_settings)
                    .getActivityIntent(requireContext())
                startActivity(intent)
                false
            }
        }
    }

    private fun getQualityIcon(quality: PrefferedPlayerQuality): Drawable? {
        val iconRes = when (quality) {
            PrefferedPlayerQuality.SD -> R.drawable.ic_quality_sd_base
            PrefferedPlayerQuality.HD -> R.drawable.ic_quality_hd_base
            PrefferedPlayerQuality.FULL_HD -> R.drawable.ic_quality_full_hd_base
            else -> return null
        }
        return context?.let { ContextCompat.getDrawable(it, iconRes) }
    }

    private fun getQualityTitle(quality: PrefferedPlayerQuality): String {
        return when (quality) {
            PrefferedPlayerQuality.SD -> "480p"
            PrefferedPlayerQuality.HD -> "720p"
            PrefferedPlayerQuality.FULL_HD -> "1080p"
            PrefferedPlayerQuality.NOT_SELECTED -> "Не выбрано"
            PrefferedPlayerQuality.ALWAYS_ASK -> "Спрашивать всегда"
            else -> ""
        }
    }

    private fun getPlayerTypeTitle(playerType: PrefferedPlayerType): String {
        return when (playerType) {
            PrefferedPlayerType.EXTERNAL -> "Внешний плеер"
            PrefferedPlayerType.INTERNAL -> "Внутренний плеер"
            PrefferedPlayerType.NOT_SELECTED -> "Не выбрано"
            PrefferedPlayerType.ALWAYS_ASK -> "Спрашивать всегда"
            else -> ""
        }
    }
}
