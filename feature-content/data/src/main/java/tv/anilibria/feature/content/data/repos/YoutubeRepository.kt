package tv.anilibria.feature.content.data.repos

import toothpick.InjectConstructor
import tv.anilibria.feature.content.data.remote.datasource.remote.api.YoutubeRemoteDataSource
import tv.anilibria.feature.content.types.Page
import tv.anilibria.feature.content.types.youtube.Youtube

@InjectConstructor
class YoutubeRepository(
    private val youtubeApi: YoutubeRemoteDataSource
) {

    suspend fun getYoutubeList(page: Int): Page<Youtube> {
        return youtubeApi.getYoutubeList(page)
    }
}