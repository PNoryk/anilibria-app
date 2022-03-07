package tv.anilibria.feature.content.data.restapi.datasource.remote.api

import tv.anilibria.feature.content.data.restapi.datasource.remote.retrofit.YoutubeApi
import tv.anilibria.feature.content.data.restapi.entity.mapper.toDomain
import tv.anilibria.feature.domain.entity.Page
import tv.anilibria.feature.domain.entity.youtube.Youtube
import tv.anilibria.plugin.data.network.NetworkWrapper
import tv.anilibria.plugin.data.network.formBodyOf
import tv.anilibria.plugin.data.restapi.handleApiResponse
import javax.inject.Inject

class YoutubeRemoteDataSource @Inject constructor(
    private val youtubeApi: NetworkWrapper<YoutubeApi>
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