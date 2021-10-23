package ru.radiationx.anilibria.ui.activities.player

import android.annotation.TargetApi
import android.app.PendingIntent
import android.app.PictureInPictureParams
import android.app.RemoteAction
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Rect
import android.graphics.drawable.Icon
import android.os.Build
import android.util.Rational
import androidx.appcompat.app.AppCompatActivity
import ru.radiationx.anilibria.R

@TargetApi(Build.VERSION_CODES.O)
class PlayerPipControllerImpl(
    private val activity: AppCompatActivity,
    private val playPauseListener: () -> Unit,
    private val prevListener: () -> Unit,
    private val nextListener: () -> Unit,
) {

    companion object {
        const val ACTION_REMOTE_CONTROL = "action.remote.control"
        const val EXTRA_REMOTE_CONTROL = "extra.remote.control"

        const val REMOTE_CONTROL_PLAY = 1
        const val REMOTE_CONTROL_PAUSE = 2
        const val REMOTE_CONTROL_PREV = 3
        const val REMOTE_CONTROL_NEXT = 4
    }

    private val currentParams = PictureInPictureParams.Builder()

    private val controlsReceiver by lazy {
        PipControlsReceiver(playPauseListener, prevListener, nextListener)
    }

    fun onDestroy() {
        try {
            activity.unregisterReceiver(controlsReceiver)
        } catch (ignore: Throwable) {
        }
    }

    fun onModeChanged(isInPictureInPictureMode: Boolean) {
        if (isInPictureInPictureMode) {
            activity.registerReceiver(controlsReceiver, IntentFilter(ACTION_REMOTE_CONTROL))
        } else {
            try {
                activity.unregisterReceiver(controlsReceiver)
            } catch (ignore: Throwable) {
            }
        }
    }

    fun isActive(): Boolean = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        activity.isInPictureInPictureMode
    } else {
        false
    }

    fun updateRect(rect: Rect) {
        currentParams.setAspectRatio(Rational(rect.width(), rect.height()))
        currentParams.setSourceRectHint(rect)
        activity.setPictureInPictureParams(currentParams.build())
    }

    fun updatePlayingState(isPlaying: Boolean) {
        val actions = mutableListOf<RemoteAction>()
        val maxActions = activity.maxNumPictureInPictureActions

        if (actions.size < maxActions) {
            val icRes = if (isPlaying) {
                R.drawable.ic_pause_remote
            } else {
                R.drawable.ic_play_arrow_remote
            }
            val actionCode = if (isPlaying) {
                REMOTE_CONTROL_PAUSE
            } else {
                REMOTE_CONTROL_PLAY
            }
            val title = if (isPlaying) {
                "Пауза"
            } else {
                "Пуск"
            }
            val action = createRemoteAction(icRes, title, actionCode)
            actions.add(action)
        }

        if (actions.size < maxActions) {
            val icRes = R.drawable.ic_skip_next_remote
            val actionCode = REMOTE_CONTROL_NEXT
            val title = "Следующая серия"
            val action = createRemoteAction(icRes, title, actionCode)
            actions.add(action)
        }

        if (actions.size < maxActions) {
            val icRes = R.drawable.ic_skip_previous_remote
            val actionCode = REMOTE_CONTROL_PREV
            val title = "Предыдущая серия"
            val action = createRemoteAction(icRes, title, actionCode)
            actions.add(0, action)
        }

        currentParams.setActions(actions)

        activity.setPictureInPictureParams(currentParams.build())
    }

    fun enterPipMode() {
        activity.enterPictureInPictureMode(currentParams.build())
    }

    fun createRemoteAction(icRes: Int, title: String, actionCode: Int) = RemoteAction(
        Icon.createWithResource(activity, icRes),
        title,
        title,
        PendingIntent.getBroadcast(
            activity,
            actionCode,
            Intent(ACTION_REMOTE_CONTROL).putExtra(EXTRA_REMOTE_CONTROL, actionCode),
            0
        )
    )
}