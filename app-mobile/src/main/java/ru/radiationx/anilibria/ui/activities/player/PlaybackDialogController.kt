package ru.radiationx.anilibria.ui.activities.player

import android.content.Context
import androidx.appcompat.app.AlertDialog
import org.michaelbel.bottomsheet.BottomSheet
import ru.radiationx.anilibria.R
import ru.radiationx.anilibria.extension.getColorFromAttr
import ru.radiationx.anilibria.extension.isDark
import ru.radiationx.data.datasource.holders.AppThemeHolder

class PlaybackDialogController(
    private val context: Context,
    private val appThemeHolder: AppThemeHolder
) {

    fun showSeasonFinishDialog(
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

    fun showAskStartEpisodeDialog(
        onStartClick: () -> Unit,
        onContinueClick: () -> Unit
    ) {
        val items = arrayOf(
            "К началу" to onStartClick,
            "К последней позиции" to onContinueClick
        )
        AlertDialog.Builder(context)
            .setTitle("Перемотать")
            .setItems(items.map { it.first }.toTypedArray()) { _, which ->
                items[which].second.invoke()
            }
            .show()
    }
}