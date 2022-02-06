package tv.anilibria.module.data.network.datasource.remote.api

import com.squareup.moshi.Moshi
import io.reactivex.Single
import tv.anilibria.module.data.network.ApiClient
import tv.anilibria.module.data.network.datasource.remote.IClient
import tv.anilibria.module.data.network.datasource.remote.address.ApiConfigProvider
import tv.anilibria.module.data.network.datasource.remote.mapApiResponse
import tv.anilibria.module.data.network.entity.app.PageResponse
import tv.anilibria.module.data.network.entity.app.youtube.YoutubeResponse
import javax.inject.Inject

class YoutubeApi @Inject constructor(
    @ApiClient private val client: IClient,
    private val apiConfig: ApiConfigProvider,
    private val moshi: Moshi
) {

    fun getYoutubeList(page: Int): Single<PageResponse<YoutubeResponse>> {
        val args = mapOf(
            "query" to "youtube",
            "page" to page.toString()
        )
        return client
            .post(apiConfig.apiUrl, args)
            .mapApiResponse(moshi)
    }
}