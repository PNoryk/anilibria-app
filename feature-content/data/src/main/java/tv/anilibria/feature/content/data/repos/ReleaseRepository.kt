package tv.anilibria.feature.content.data.repos

import tv.anilibria.feature.content.data.local.ReleaseUpdateHelper
import tv.anilibria.feature.content.data.remote.datasource.remote.api.ReleaseRemoteDataSource
import tv.anilibria.feature.content.types.Page
import tv.anilibria.feature.content.types.release.RandomRelease
import tv.anilibria.feature.content.types.release.Release
import tv.anilibria.feature.content.types.release.ReleaseCode
import tv.anilibria.feature.content.types.release.ReleaseId
import javax.inject.Inject

/**
 * Created by radiationx on 17.12.17.
 */
class ReleaseRepository @Inject constructor(
    private val releaseApi: ReleaseRemoteDataSource,
    private val releaseUpdateHolder: ReleaseUpdateHelper
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
