package tv.anilibria.module.data.repos

import tv.anilibria.module.data.ReleaseUpdateHolder
import tv.anilibria.module.data.restapi.datasource.remote.api.ReleaseRemoteDataSource
import tv.anilibria.module.domain.entity.Page
import tv.anilibria.module.domain.entity.release.RandomRelease
import tv.anilibria.module.domain.entity.release.Release
import tv.anilibria.module.domain.entity.release.ReleaseCode
import tv.anilibria.module.domain.entity.release.ReleaseId
import javax.inject.Inject

/**
 * Created by radiationx on 17.12.17.
 */
class ReleaseRepository @Inject constructor(
    private val releaseApi: ReleaseRemoteDataSource,
    private val releaseUpdateHolder: ReleaseUpdateHolder
) {

    suspend fun getRandomRelease(): RandomRelease {
        return releaseApi.getRandomRelease()
    }

    suspend fun getRelease(releaseId: ReleaseId): Release {
        return releaseApi.getRelease(releaseId.id).also {
            releaseUpdateHolder.update(listOf(it))
        }
    }

    suspend fun getRelease(code: ReleaseCode): Release {
        return releaseApi.getRelease(code.code).also {
            releaseUpdateHolder.update(listOf(it))
        }
    }

    suspend fun getReleasesById(ids: List<ReleaseId>): List<Release> {
        return releaseApi.getReleasesByIds(ids.map { it.id }).also {
            releaseUpdateHolder.update(it)
        }
    }

    suspend fun getReleases(page: Int): Page<Release> {
        return releaseApi.getReleases(page).also {
            releaseUpdateHolder.update(it.items)
        }
    }
}
