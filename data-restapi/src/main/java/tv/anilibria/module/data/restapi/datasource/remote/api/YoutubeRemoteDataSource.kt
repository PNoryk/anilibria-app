package tv.anilibria.module.data.restapi.datasource.remote.api

import tv.anilibria.module.data.restapi.datasource.remote.retrofit.YoutubeApi
import tv.anilibria.module.data.restapi.entity.mapper.toDomain
import tv.anilibria.module.domain.entity.Page
import tv.anilibria.module.domain.entity.youtube.Youtube
import tv.anilibria.plugin.data.network.formBodyOf
import tv.anilibria.plugin.data.restapi.ApiWrapper
import tv.anilibria.plugin.data.restapi.handleApiResponse
import javax.inject.Inject

class YoutubeRemoteDataSource @Inject constructor(
    private val youtubeApi: ApiWrapper<YoutubeApi>
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