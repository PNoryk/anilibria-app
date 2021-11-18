package ru.radiationx.anilibria.ui.activities.player.controllers

interface PlayerPlaybackInfo {

    fun isPlaying(): Boolean

    fun getPosition(): PlaybackPosition

    fun getEndPosition(): PlaybackPosition

    fun getSpeed(): Float
}