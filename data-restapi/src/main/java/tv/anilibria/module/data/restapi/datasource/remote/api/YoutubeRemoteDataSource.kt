package tv.anilibria.module.data.restapi.datasource.remote.api

import io.reactivex.Single
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

    fun getYoutubeList(page: Int): Single<Page<Youtube>> {
        val args = formBodyOf(
            "query" to "youtube",
            "page" to page.toString()
        )
        return youtubeApi.proxy()
            .getYoutube(args)
            .handleApiResponse()
            .map { pageResponse -> pageResponse.toDomain { it.toDomain() } }
    }
}