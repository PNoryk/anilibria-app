package ru.radiationx.anilibria.ui.activities.player.controllers

data class PlaybackState(
    val position: PlaybackPosition,
    val endPosition: PlaybackPosition,
    val isPlaying: Boolean
) {

    fun getPercent(): Float {
        if (endPosition.position <= 0) {
            return 0f
        }
        return (position.position / endPosition.position.toDouble()).toFloat()
    }
}