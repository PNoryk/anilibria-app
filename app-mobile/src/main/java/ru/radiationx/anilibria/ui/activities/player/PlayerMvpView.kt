package ru.radiationx.anilibria.ui.activities.player

import moxy.MvpView
import ru.radiationx.anilibria.ui.activities.player.controllers.PlaybackPosition

interface PlayerMvpView : MvpView {

    fun setReleaseInfo(releaseTitle: String)
    fun setEpisodeInfo(episodeTitle: String, quality: PlayerQuality)

    fun startPlayer(url: String, position: PlaybackPosition)
    fun playPlayer()
    fun pausePlayer()
    fun stopPlayer()

    fun closeScreen()
}