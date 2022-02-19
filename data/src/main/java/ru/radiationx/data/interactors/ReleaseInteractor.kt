package ru.radiationx.data.interactors

import ru.radiationx.data.datasource.holders.EpisodesCheckerHolder
import ru.radiationx.data.datasource.holders.PreferencesHolder
import ru.radiationx.data.entity.app.release.ReleaseFull
import javax.inject.Inject

/**
 * Created by radiationx on 17.02.18.
 */
@Deprecated("old data")
class ReleaseInteractor @Inject constructor(
    private val episodesCheckerStorage: EpisodesCheckerHolder,
    private val preferencesHolder: PreferencesHolder,
) {


    /* Common */
    fun putEpisode(episode: ReleaseFull.Episode) = episodesCheckerStorage.putEpisode(episode)


    fun getEpisodes(releaseId: Int) = episodesCheckerStorage.getEpisodes(releaseId)


    fun getPlaySpeed() = preferencesHolder.playSpeed

    fun setPlaySpeed(value: Float) {
        preferencesHolder.playSpeed = value
    }


    fun getPIPControl() = preferencesHolder.pipControl

    fun setPIPControl(value: Int) {
        preferencesHolder.pipControl = value
    }

}