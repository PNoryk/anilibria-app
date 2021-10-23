package ru.radiationx.anilibria.ui.activities.player

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class PipControlsReceiver(
    private val playPauseListener: () -> Unit,
    private val prevListener: () -> Unit,
    private val nextListener: () -> Unit,
) : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent?) {
        if (intent == null || intent.action != PlayerPipControllerImpl.ACTION_REMOTE_CONTROL) {
            return
        }
        when (intent.getIntExtra(PlayerPipControllerImpl.EXTRA_REMOTE_CONTROL, -1)) {
            PlayerPipControllerImpl.REMOTE_CONTROL_PLAY,
            PlayerPipControllerImpl.REMOTE_CONTROL_PAUSE -> playPauseListener.invoke()
            PlayerPipControllerImpl.REMOTE_CONTROL_PREV -> prevListener.invoke()
            PlayerPipControllerImpl.REMOTE_CONTROL_NEXT -> nextListener.invoke()
        }
    }
}