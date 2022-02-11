package tv.anilibria.module.data.restapi.datasource.remote.api

import com.squareup.moshi.Moshi
import io.reactivex.Single
import tv.anilibria.module.data.restapi.entity.app.PageResponse
import tv.anilibria.module.data.restapi.entity.app.youtube.YoutubeResponse
import tv.anilibria.module.data.restapi.entity.mapper.toDomain
import tv.anilibria.module.domain.entity.Page
import tv.anilibria.module.domain.entity.youtube.Youtube
import tv.anilibria.plugin.data.restapi.ApiNetworkClient
import tv.anilibria.plugin.data.restapi.ApiConfigProvider
import tv.anilibria.plugin.data.restapi.mapApiResponse
import javax.inject.Inject

class YoutubeRemoteDataSource @Inject constructor(
    private val apiClient: ApiNetworkClient,
    private val apiConfig: ApiConfigProvider,
    private val moshi: Moshi
) {

    fun getYoutubeList(page: Int): Single<Page<Youtube>> {
        val args = mapOf(
            "query" to "youtube",
            "page" to page.toString()
        )
        return apiClient
            .post(apiConfig.apiUrl, args)
            .mapApiResponse<PageResponse<YoutubeResponse>>(moshi)
            .map { pageResponse -> pageResponse.toDomain { it.toDomain() } }
    }
}