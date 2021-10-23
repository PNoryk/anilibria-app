package ru.radiationx.anilibria.ui.activities.player

import android.content.pm.ActivityInfo
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity

class PlayerFullscreenController(
    private val activity: AppCompatActivity,
    private val onChanged: (Boolean) -> Unit
) {

    private var fullscreenOrientation = false

    fun updateConfig() {
        fullscreenOrientation = when (activity.resources.configuration.orientation) {
            Configuration.ORIENTATION_LANDSCAPE -> true
            else -> false
        }
        onChanged.invoke(fullscreenOrientation)
    }

    fun toggle() {
        if (fullscreenOrientation) {
            activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
        } else {
            activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE
        }
        fullscreenOrientation = !fullscreenOrientation
    }

}