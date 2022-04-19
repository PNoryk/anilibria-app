package tv.anilibria.feature.content.data.remote.datasource.remote.api

import toothpick.InjectConstructor
import tv.anilibria.feature.content.data.remote.datasource.remote.retrofit.YoutubeApiWrapper
import tv.anilibria.feature.content.data.remote.entity.mapper.toDomain
import tv.anilibria.feature.content.types.Page
import tv.anilibria.feature.content.types.youtube.Youtube
import tv.anilibria.plugin.data.network.formBodyOf
import tv.anilibria.plugin.data.restapi.handleApiResponse

@InjectConstructor
class YoutubeRemoteDataSource(
    private val youtubeApi: YoutubeApiWrapper
) {

    suspend fun getYoutubeList(page: Int): Page<Youtube> {
        val args = formBodyOf(
            "query" to "youtube",
            "page" to page.toString()
        )
        return youtubeApi.proxy()
            .getYoutube(args)
            .handleApiResponse()
            .toDomain { it.toDomain() }
    }
}