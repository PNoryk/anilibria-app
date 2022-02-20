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
import ru.radiationx.anilibria.utils.Utils
import ru.radiationx.data.datasource.remote.Api
import ru.radiationx.data.datasource.remote.address.ApiConfig
import ru.radiationx.shared_app.di.injectDependencies
import tv.anilibria.module.data.analytics.AnalyticsConstants
import tv.anilibria.module.data.analytics.features.SettingsAnalytics
import tv.anilibria.module.data.analytics.features.mapper.toAnalyticsPlayer
import tv.anilibria.module.data.analytics.features.mapper.toAnalyticsQuality
import tv.anilibria.module.data.analytics.features.model.AnalyticsAppTheme
import tv.anilibria.module.data.preferences.PlayerQuality
import tv.anilibria.module.data.preferences.PlayerType
import tv.anilibria.module.data.preferences.PreferencesStorage
import tv.anilibria.plugin.shared.appinfo.SharedBuildConfig
import javax.inject.Inject

/**
 * Created by radiationx on 25.12.16.
 */

class SettingsFragment : BaseSettingFragment() {

    @Inject
    lateinit var preferencesStorage: PreferencesStorage

    @Inject
    lateinit var apiConfig: ApiConfig

    @Inject
    lateinit var errorHandler: IErrorHandler

    @Inject
    lateinit var settingsAnalytics: SettingsAnalytics

    @Inject
    lateinit var sharedBuildConfig: SharedBuildConfig

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
            val savedQuality = preferencesStorage.quality.blockingGet()
            icon = getQualityIcon(savedQuality)
            summary = getQualityTitle(savedQuality)
            setOnPreferenceClickListener { preference ->
                settingsAnalytics.qualityClick()
                val values = arrayOf(
                    PlayerQuality.SD,
                    PlayerQuality.HD,
                    PlayerQuality.FULL_HD,
                    PlayerQuality.NOT_SELECTED,
                    PlayerQuality.ALWAYS_ASK,
                )
                val titles = values.map { getQualityTitle(it) }.toTypedArray()
                AlertDialog.Builder(preference.context)
                    .setTitle(preference.title)
                    .setItems(titles) { _, which ->
                        val quality = values[which]
                        settingsAnalytics.qualityChange(quality.toAnalyticsQuality())
                        preferencesStorage.quality.blockingSet(quality)
                        icon = getQualityIcon(quality)
                        summary = getQualityTitle(quality)
                    }
                    .show()
                false
            }
        }

        findPreference<Preference>("player_type")?.apply {
            val savedPlayerType = preferencesStorage.playerType.blockingGet()
            icon = this.context.getCompatDrawable(R.drawable.ic_play_circle_outline)
            summary = getPlayerTypeTitle(savedPlayerType)
            setOnPreferenceClickListener { preference ->
                settingsAnalytics.playerClick()
                val values = arrayOf(
                    PlayerType.EXTERNAL,
                    PlayerType.INTERNAL,
                    PlayerType.NOT_SELECTED,
                    PlayerType.ALWAYS_ASK
                )
                val titles = values.map { getPlayerTypeTitle(it) }.toTypedArray()
                AlertDialog.Builder(preference.context)
                    .setTitle(preference.title)
                    .setItems(titles) { dialog, which ->
                        val playerType = values[which]
                        settingsAnalytics.playerChange(playerType.toAnalyticsPlayer())
                        preferencesStorage.playerType.blockingSet(playerType)
                        summary = getPlayerTypeTitle(playerType)
                    }
                    .show()
                false
            }
        }

        findPreference<Preference>("about.application")?.apply {
            val appendix = if (Api.STORE_APP_IDS.contains(sharedBuildConfig.applicationId)) {
                " для Play Market"
            } else {
                ""
            }
            summary = "Версия ${sharedBuildConfig.versionName}$appendix"
        }

        findPreference<Preference>("about.app_other_apps")?.apply {
            icon = this.context.getCompatDrawable(R.drawable.ic_anilibria)
            setOnPreferenceClickListener {
                settingsAnalytics.otherAppsClick()
                Utils.externalLink("https://anilibria.app/")
                false
            }
        }

        findPreference<Preference>("about.app_topic_4pda")?.apply {
            icon = this.context.getCompatDrawable(R.drawable.ic_4pda)
            setOnPreferenceClickListener {
                settingsAnalytics.fourPdaClick()
                Utils.externalLink("http://4pda.ru/forum/index.php?showtopic=886616")
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

    private fun getQualityIcon(quality: PlayerQuality): Drawable? {
        val iconRes = when (quality) {
            PlayerQuality.SD -> R.drawable.ic_quality_sd_base
            PlayerQuality.HD -> R.drawable.ic_quality_hd_base
            PlayerQuality.FULL_HD -> R.drawable.ic_quality_full_hd_base
            else -> return null
        }
        return context?.let { ContextCompat.getDrawable(it, iconRes) }
    }

    private fun getQualityTitle(quality: PlayerQuality): String {
        return when (quality) {
            PlayerQuality.SD -> "480p"
            PlayerQuality.HD -> "720p"
            PlayerQuality.FULL_HD -> "1080p"
            PlayerQuality.NOT_SELECTED -> "Не выбрано"
            PlayerQuality.ALWAYS_ASK -> "Спрашивать всегда"
            else -> ""
        }
    }

    private fun getPlayerTypeTitle(playerType: PlayerType): String {
        return when (playerType) {
            PlayerType.EXTERNAL -> "Внешний плеер"
            PlayerType.INTERNAL -> "Внутренний плеер"
            PlayerType.NOT_SELECTED -> "Не выбрано"
            PlayerType.ALWAYS_ASK -> "Спрашивать всегда"
            else -> ""
        }
    }
}
