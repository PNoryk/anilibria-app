package tv.anilibria.module.domain.remote

import io.reactivex.Single
import tv.anilibria.module.domain.entity.other.LinkMenuItem

interface MenuRemoteDataSource {
    fun getMenu(): Single<List<LinkMenuItem>>
}