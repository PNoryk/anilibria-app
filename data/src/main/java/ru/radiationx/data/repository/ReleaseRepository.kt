package ru.radiationx.data.repository

import android.util.Log
import io.reactivex.Single
import ru.radiationx.shared.ktx.SchedulersProvider
import ru.radiationx.data.datasource.holders.ReleaseUpdateHolder
import ru.radiationx.data.datasource.remote.api.ReleaseApi
import ru.radiationx.data.entity.app.Paginated
import ru.radiationx.data.entity.app.release.RandomRelease
import ru.radiationx.data.entity.app.release.ReleaseFull
import ru.radiationx.data.entity.app.release.ReleaseItem
import ru.radiationx.data.entity.app.release.ReleaseUpdate
import javax.inject.Inject

/**
 * Created by radiationx on 17.12.17.
 */
@Deprecated("old data")
class ReleaseRepository @Inject constructor(
    private val schedulers: SchedulersProvider,
    private val releaseApi: ReleaseApi,
    private val releaseUpdateHolder: ReleaseUpdateHolder
) {

    fun getRelease(releaseId: Int): Single<ReleaseFull> = releaseApi
        .getRelease(releaseId)
        .subscribeOn(schedulers.io())
        .observeOn(schedulers.ui())

    fun getRelease(releaseIdName: String): Single<ReleaseFull> = releaseApi
        .getRelease(releaseIdName)
        .subscribeOn(schedulers.io())
        .observeOn(schedulers.ui())
}
