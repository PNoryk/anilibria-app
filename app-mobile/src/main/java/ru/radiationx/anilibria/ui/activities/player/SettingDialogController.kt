package ru.radiationx.anilibria.ui.activities.player

import android.content.Context
import android.graphics.PorterDuff
import android.text.Html
import androidx.core.content.ContextCompat
import com.devbrackets.android.exomedia.core.video.scale.ScaleType
import org.michaelbel.bottomsheet.BottomSheet
import ru.radiationx.anilibria.R
import ru.radiationx.anilibria.extension.getColorFromAttr
import ru.radiationx.anilibria.extension.isDark
import ru.radiationx.data.analytics.features.PlayerAnalytics
import ru.radiationx.data.analytics.features.mapper.toAnalyticsPip
import ru.radiationx.data.analytics.features.mapper.toAnalyticsScale
import ru.radiationx.data.datasource.holders.AppThemeHolder
import ru.radiationx.data.datasource.holders.PreferencesHolder
import ru.radiationx.data.entity.app.release.ReleaseFull

class SettingDialogController(
    private val playerAnalytics: PlayerAnalytics,
    private val appThemeHolder: AppThemeHolder,
    private val qualityListener: (Int) -> Unit,
    private val speedListener: (Float) -> Unit,
    private val scaleListener: (ScaleType) -> Unit,
    private val pipListener: (Int) -> Unit
) {
    private val settingQuality = 0
    private val settingPlaySpeed = 1
    private val settingScale = 2
    private val settingPIP = 3

    private var openedDialogs = mutableListOf<BottomSheet>()

    private fun BottomSheet.register() = openedDialogs.add(this)

    //todo move to smh
    fun getQualityTitle(quality: Int) = when (quality) {
        MyPlayerActivity.VAL_QUALITY_SD -> "480p"
        MyPlayerActivity.VAL_QUALITY_HD -> "720p"
        MyPlayerActivity.VAL_QUALITY_FULL_HD -> "1080p"
        else -> "Вероятнее всего 480p"
    }

    //todo move to smh
    fun getPlaySpeedTitle(speed: Float) = if (speed == 1.0f) {
        "Обычная"
    } else {
        "${"$speed".trimEnd('0').trimEnd('.').trimEnd(',')}x"
    }

    //todo move to smh
    fun getScaleTitle(scale: ScaleType) = when (scale) {
        ScaleType.FIT_CENTER -> "Оптимально"
        ScaleType.CENTER_CROP -> "Обрезать"
        ScaleType.FIT_XY -> "Растянуть"
        else -> "Одному лишь богу известно"
    }

    //todo move to smh
    fun getPIPTitle(pipControl: Int) = when (pipControl) {
        PreferencesHolder.PIP_AUTO -> "При скрытии экрана"
        PreferencesHolder.PIP_BUTTON -> "По кнопке"
        else -> "Одному лишь богу известно"
    }

    fun clearAllDialogs() {
        if (openedDialogs.isNotEmpty()) {
            openedDialogs.forEach {
                it.dismiss()
            }
            openedDialogs.clear()
        }
    }

    fun showSettingsDialog(
        context: Context,
        episode: ReleaseFull.Episode,
        currentQuality: Int,
        currentPlaySpeed: Float,
        currentScale: ScaleType,
        currentPipControl: Int,
        currentIsSausage: Boolean,
        currentIsPipMode: Boolean,
    ) {
        clearAllDialogs()

        val qualityValue = getQualityTitle(currentQuality)
        val speedValue = getPlaySpeedTitle(currentPlaySpeed)
        val scaleValue = getScaleTitle(currentScale)
        val pipValue = getPIPTitle(currentPipControl)

        val valuesList = mutableListOf(
            settingQuality,
            settingPlaySpeed
        )
        if (currentIsSausage) {
            valuesList.add(settingScale)
        }
        if (currentIsPipMode) {
            valuesList.add(settingPIP)
        }

        val titles = valuesList
            .asSequence()
            .map {
                when (it) {
                    settingQuality -> "Качество (<b>$qualityValue</b>)"
                    settingPlaySpeed -> "Скорость (<b>$speedValue</b>)"
                    settingScale -> "Соотношение сторон (<b>$scaleValue</b>)"
                    settingPIP -> "Режим окна (<b>$pipValue</b>)"
                    else -> "Привет"
                }
            }
            .map { Html.fromHtml(it) }
            .toList()
            .toTypedArray()

        val icQualityRes = when (currentQuality) {
            MyPlayerActivity.VAL_QUALITY_SD -> R.drawable.ic_quality_sd_base
            MyPlayerActivity.VAL_QUALITY_HD -> R.drawable.ic_quality_hd_base
            MyPlayerActivity.VAL_QUALITY_FULL_HD -> R.drawable.ic_quality_full_hd_base
            else -> R.drawable.ic_settings
        }
        val icons = valuesList
            .asSequence()
            .map {
                when (it) {
                    settingQuality -> icQualityRes
                    settingPlaySpeed -> R.drawable.ic_play_speed
                    settingScale -> R.drawable.ic_aspect_ratio
                    settingPIP -> R.drawable.ic_picture_in_picture_alt
                    else -> R.drawable.ic_anilibria
                }
            }
            .map {
                ContextCompat.getDrawable(context, it)
            }
            .toList()
            .toTypedArray()

        BottomSheet.Builder(context)
            .setItems(titles, icons) { _, which ->
                when (valuesList[which]) {
                    settingQuality -> {
                        playerAnalytics.settingsQualityClick()
                        showQualityDialog(context, episode, currentQuality)
                    }
                    settingPlaySpeed -> {
                        playerAnalytics.settingsSpeedClick()
                        showPlaySpeedDialog(context, currentPlaySpeed)
                    }
                    settingScale -> {
                        playerAnalytics.settingsScaleClick()
                        showScaleDialog(context, currentScale)
                    }
                    settingPIP -> {
                        playerAnalytics.settingsPipClick()
                        showPIPDialog(context, currentPipControl)
                    }
                }
            }
            .setDarkTheme(appThemeHolder.getTheme().isDark())
            .setIconTintMode(PorterDuff.Mode.SRC_ATOP)
            .setIconColor(context.getColorFromAttr(R.attr.colorOnSurface))
            .setItemTextColor(context.getColorFromAttr(R.attr.textDefault))
            .setBackgroundColor(context.getColorFromAttr(R.attr.colorSurface))
            .show()
            .register()
    }

    fun showPlaySpeedDialog(
        context: Context,
        currentPlaySpeed: Float
    ) {
        val values = arrayOf(
            0.25f,
            0.5f,
            0.75f,
            1.0f,
            1.25f,
            1.5f,
            1.75f,
            2.0f
        )
        val activeIndex = values.indexOf(currentPlaySpeed)
        val titles = values
            .mapIndexed { index, s ->
                val stringValue = getPlaySpeedTitle(s)
                when (index) {
                    activeIndex -> "<b>$stringValue</b>"
                    else -> stringValue
                }
            }
            .map { Html.fromHtml(it) }
            .toTypedArray()

        BottomSheet.Builder(context)
            .setTitle("Скорость воспроизведения")
            .setItems(titles) { _, which ->
                speedListener.invoke(values[which])
            }
            .setDarkTheme(appThemeHolder.getTheme().isDark())
            .setItemTextColor(context.getColorFromAttr(R.attr.textDefault))
            .setTitleTextColor(context.getColorFromAttr(R.attr.textSecond))
            .setBackgroundColor(context.getColorFromAttr(R.attr.colorSurface))
            .show()
            .register()
    }

    fun showQualityDialog(
        context: Context,
        episode: ReleaseFull.Episode,
        currentQuality: Int
    ) {
        val qualities = mutableListOf<Int>()
        if (episode.urlSd != null) qualities.add(MyPlayerActivity.VAL_QUALITY_SD)
        if (episode.urlHd != null) qualities.add(MyPlayerActivity.VAL_QUALITY_HD)
        if (episode.urlFullHd != null) qualities.add(MyPlayerActivity.VAL_QUALITY_FULL_HD)

        val values = qualities.toTypedArray()

        val activeIndex = values.indexOf(currentQuality)
        val titles = values
            .mapIndexed { index, s ->
                val stringValue = getQualityTitle(s)
                if (index == activeIndex) "<b>$stringValue</b>" else stringValue
            }
            .map { Html.fromHtml(it) }
            .toTypedArray()

        BottomSheet.Builder(context)
            .setTitle("Качество")
            .setItems(titles) { _, which ->
                qualityListener.invoke(values[which])
            }
            .setDarkTheme(appThemeHolder.getTheme().isDark())
            .setItemTextColor(context.getColorFromAttr(R.attr.textDefault))
            .setTitleTextColor(context.getColorFromAttr(R.attr.textSecond))
            .setBackgroundColor(context.getColorFromAttr(R.attr.colorSurface))
            .show()
            .register()
    }

    fun showScaleDialog(
        context: Context,
        currentScale: ScaleType
    ) {
        val values = arrayOf(
            ScaleType.FIT_CENTER,
            ScaleType.CENTER_CROP,
            ScaleType.FIT_XY
        )
        val activeIndex = values.indexOf(currentScale)
        val titles = values
            .mapIndexed { index, s ->
                val stringValue = getScaleTitle(s)
                if (index == activeIndex) "<b>$stringValue</b>" else stringValue
            }
            .map { Html.fromHtml(it) }
            .toTypedArray()

        BottomSheet.Builder(context)
            .setTitle("Соотношение сторон")
            .setItems(titles) { _, which ->
                val newScaleType = values[which]
                playerAnalytics.settingsScaleChange(newScaleType.ordinal.toAnalyticsScale())
                scaleListener.invoke(newScaleType)
            }
            .setDarkTheme(appThemeHolder.getTheme().isDark())
            .setItemTextColor(context.getColorFromAttr(R.attr.textDefault))
            .setTitleTextColor(context.getColorFromAttr(R.attr.textSecond))
            .setBackgroundColor(context.getColorFromAttr(R.attr.colorSurface))
            .show()
            .register()
    }

    fun showPIPDialog(
        context: Context,
        currentPipControl: Int
    ) {
        val values = arrayOf(
            PreferencesHolder.PIP_AUTO,
            PreferencesHolder.PIP_BUTTON
        )
        val activeIndex = values.indexOf(currentPipControl)
        val titles = values
            .mapIndexed { index, s ->
                val stringValue = getPIPTitle(s)
                if (index == activeIndex) "<b>$stringValue</b>" else stringValue
            }
            .map { Html.fromHtml(it) }
            .toTypedArray()

        BottomSheet.Builder(context)
            .setTitle("Режим окна (картинка в картинке)")
            .setItems(titles) { _, which ->
                val newPipControl = values[which]
                playerAnalytics.settingsPipChange(newPipControl.toAnalyticsPip())
                pipListener.invoke(newPipControl)
            }
            .setDarkTheme(appThemeHolder.getTheme().isDark())
            .setItemTextColor(context.getColorFromAttr(R.attr.textDefault))
            .setTitleTextColor(context.getColorFromAttr(R.attr.textSecond))
            .setBackgroundColor(context.getColorFromAttr(R.attr.colorSurface))
            .show()
            .register()
    }
}