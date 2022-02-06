package tv.anilibria.module.data.network.datasource.remote.api

import io.reactivex.Single
import tv.anilibria.module.domain.entity.other.LinkMenuItem

interface MenuRemoteDataSource {
    fun getMenu(): Single<List<LinkMenuItem>>
}