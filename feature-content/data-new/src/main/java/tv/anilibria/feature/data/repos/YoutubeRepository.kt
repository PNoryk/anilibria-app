package tv.anilibria.feature.data.repos

import tv.anilibria.feature.data.restapi.datasource.remote.api.YoutubeRemoteDataSource
import tv.anilibria.feature.domain.entity.Page
import tv.anilibria.feature.domain.entity.youtube.Youtube
import javax.inject.Inject

class YoutubeRepository @Inject constructor(
    private val youtubeApi: YoutubeRemoteDataSource
) {

    suspend fun getYoutubeList(page: Int): Page<Youtube> {
        return youtubeApi.getYoutubeList(page)
    }
}