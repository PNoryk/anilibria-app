package tv.anilibria.module.domain.remote

import io.reactivex.Single
import tv.anilibria.module.domain.entity.Page
import tv.anilibria.module.domain.entity.youtube.Youtube

interface YoutubeRemoteDataSource {
    fun getYoutubeList(page: Int): Single<Page<Youtube>>
}