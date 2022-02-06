package tv.anilibria.module.data.network.datasource.remote.api

import com.squareup.moshi.Moshi
import io.reactivex.Single
import tv.anilibria.module.data.network.ApiClient
import tv.anilibria.module.data.network.datasource.remote.IClient
import tv.anilibria.module.data.network.datasource.remote.ApiConfigProvider
import tv.anilibria.module.data.network.datasource.remote.mapApiResponse
import tv.anilibria.module.data.network.entity.app.PageResponse
import tv.anilibria.module.data.network.entity.app.youtube.YoutubeResponse
import tv.anilibria.module.data.network.entity.mapper.toDomain
import tv.anilibria.module.domain.entity.Page
import tv.anilibria.module.domain.entity.youtube.Youtube
import tv.anilibria.module.domain.remote.YoutubeRemoteDataSource
import javax.inject.Inject

class YoutubeRemoteDataSourceImpl @Inject constructor(
    @ApiClient private val client: IClient,
    private val apiConfig: ApiConfigProvider,
    private val moshi: Moshi
) : YoutubeRemoteDataSource {

    override fun getYoutubeList(page: Int): Single<Page<Youtube>> {
        val args = mapOf(
            "query" to "youtube",
            "page" to page.toString()
        )
        return client
            .post(apiConfig.apiUrl, args)
            .mapApiResponse<PageResponse<YoutubeResponse>>(moshi)
            .map { pageResponse -> pageResponse.toDomain { it.toDomain() } }
    }
}