package tv.anilibria.module.data

import tv.anilibria.module.domain.entity.release.Release

interface ReleaseUpdateHolder {

    suspend fun update(data: List<Release>)
}