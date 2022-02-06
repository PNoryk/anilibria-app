package tv.anilibria.module.data.network.datasource.remote.api

import io.reactivex.Single
import tv.anilibria.module.domain.entity.Page
import tv.anilibria.module.domain.entity.youtube.Youtube

interface YoutubeRemoteDataSource {
    fun getYoutubeList(page: Int): Single<Page<Youtube>>
}