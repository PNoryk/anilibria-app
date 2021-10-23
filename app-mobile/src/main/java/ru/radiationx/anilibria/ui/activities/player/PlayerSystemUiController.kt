package ru.radiationx.anilibria.ui.activities.player

import android.view.View
import androidx.appcompat.app.AppCompatActivity

class PlayerSystemUiController(
    private val activity: AppCompatActivity,
    private val onChangeVisibility: (Boolean) -> Unit
) {

    private val fullScreenListener = FullScreenListener(onChangeVisibility)
    private val flagsHelper = PlayerWindowFlagHelper
    private var currentFullscreen = false


    fun onCreate() {
        activity.window.decorView.setOnSystemUiVisibilityChangeListener(fullScreenListener)
        goFullscreen()
    }

    fun onDestroy() {
        activity.window.decorView.setOnSystemUiVisibilityChangeListener(null)
        exitFullscreen()
    }

    fun updateConfig() {
        activity.window.decorView.systemUiVisibility = flagsHelper.getFlags(
            activity.resources.configuration.orientation,
            currentFullscreen
        )
    }

    fun goFullscreen() {
        currentFullscreen = true
        updateConfig()
    }

    fun exitFullscreen() {
        currentFullscreen = false
        updateConfig()
    }

    private class FullScreenListener(
        private val onChange: (Boolean) -> Unit
    ) : View.OnSystemUiVisibilityChangeListener {

        private var currentIsVisible: Boolean? = null

        override fun onSystemUiVisibilityChange(visibility: Int) {
            val isVisible = visibility and View.SYSTEM_UI_FLAG_FULLSCREEN == 0
            if (currentIsVisible != isVisible) {
                currentIsVisible = isVisible
                onChange.invoke(isVisible)
            }
        }
    }

}