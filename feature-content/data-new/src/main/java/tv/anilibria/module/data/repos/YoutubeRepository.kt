package tv.anilibria.module.data.repos

import tv.anilibria.module.data.restapi.datasource.remote.api.YoutubeRemoteDataSource
import tv.anilibria.module.domain.entity.Page
import tv.anilibria.module.domain.entity.youtube.Youtube
import javax.inject.Inject

class YoutubeRepository @Inject constructor(
    private val youtubeApi: YoutubeRemoteDataSource
) {

    suspend fun getYoutubeList(page: Int): Page<Youtube> {
        return youtubeApi.getYoutubeList(page)
    }
}