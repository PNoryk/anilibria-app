package ru.radiationx.anilibria.ui.activities.player

import android.content.Context
import org.michaelbel.bottomsheet.BottomSheet
import ru.radiationx.anilibria.R
import ru.radiationx.anilibria.extension.getColorFromAttr
import ru.radiationx.anilibria.extension.isDark
import ru.radiationx.data.analytics.features.PlayerAnalytics
import ru.radiationx.data.datasource.holders.AppThemeHolder

class FinishDialogController(
    private val appThemeHolder: AppThemeHolder
) {

    fun showSeasonFinishDialog(
        context: Context,
        episodeRestartListener: () -> Unit,
        seasonRestartListener: () -> Unit,
        closePlayerListener: () -> Unit,
    ) {
        val items = arrayOf(
            "Начать серию заново" to episodeRestartListener,
            "Начать с первой серии" to seasonRestartListener,
            "Закрыть плеер" to closePlayerListener
        )
        BottomSheet.Builder(context)
            .setTitle("Серия полностью просмотрена")
            .setItems(items.map { it.first }.toTypedArray()) { _, which ->
                items[which].second.invoke()
            }
            .setDarkTheme(appThemeHolder.getTheme().isDark())
            .setItemTextColor(context.getColorFromAttr(R.attr.textDefault))
            .setTitleTextColor(context.getColorFromAttr(R.attr.textSecond))
            .setBackgroundColor(context.getColorFromAttr(R.attr.colorSurface))
            .show()
    }

    fun showEpisodeFinishDialog(
        context: Context,
        episodeRestartListener: () -> Unit,
        startNextListener: () -> Unit
    ) {
        val items = arrayOf(
            "Начать серию заново" to episodeRestartListener,
            "Включить следущую серию" to startNextListener
        )
        BottomSheet.Builder(context)
            .setTitle("Серия полностью просмотрена")
            .setItems(items.map { it.first }.toTypedArray()) { _, which ->
                items[which].second.invoke()
            }
            .setDarkTheme(appThemeHolder.getTheme().isDark())
            .setItemTextColor(context.getColorFromAttr(R.attr.textDefault))
            .setTitleTextColor(context.getColorFromAttr(R.attr.textSecond))
            .setBackgroundColor(context.getColorFromAttr(R.attr.colorSurface))
            .show()
    }
}