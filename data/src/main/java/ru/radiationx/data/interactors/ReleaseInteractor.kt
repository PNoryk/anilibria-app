package ru.radiationx.data.interactors

import android.util.Log
import com.jakewharton.rxrelay2.PublishRelay
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.functions.BiFunction
import ru.radiationx.shared.ktx.SchedulersProvider
import ru.radiationx.data.datasource.holders.EpisodesCheckerHolder
import ru.radiationx.data.datasource.holders.PreferencesHolder
import ru.radiationx.data.entity.app.Paginated
import ru.radiationx.data.entity.app.release.RandomRelease
import ru.radiationx.data.entity.app.release.ReleaseFull
import ru.radiationx.data.entity.app.release.ReleaseItem
import ru.radiationx.data.repository.ReleaseRepository
import java.util.concurrent.ConcurrentHashMap
import javax.inject.Inject

/**
 * Created by radiationx on 17.02.18.
 */
@Deprecated("old data")
class ReleaseInteractor @Inject constructor(
    private val releaseRepository: ReleaseRepository,
    private val episodesCheckerStorage: EpisodesCheckerHolder,
    private val preferencesHolder: PreferencesHolder,
    private val schedulers: SchedulersProvider
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