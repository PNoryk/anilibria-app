package ru.radiationx.anilibria.ui.activities.player.controllers


interface PlayerPlaybackController {

    fun startPlay(url: String, position: PlaybackPosition)

    fun stop()

    fun pause()

    fun play()

    fun seekTo(position: PlaybackPosition)

    fun setSpeed(speed: Float)
}
