package tv.anilibria.feature.content.data.repos

import tv.anilibria.feature.content.data.remote.datasource.remote.api.YoutubeRemoteDataSource
import tv.anilibria.feature.content.types.Page
import tv.anilibria.feature.content.types.youtube.Youtube
import javax.inject.Inject

class YoutubeRepository @Inject constructor(
    private val youtubeApi: YoutubeRemoteDataSource
) {

    suspend fun getYoutubeList(page: Int): Page<Youtube> {
        return youtubeApi.getYoutubeList(page)
    }
}