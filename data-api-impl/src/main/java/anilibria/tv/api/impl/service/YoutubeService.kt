package anilibria.tv.api.impl.service

import io.reactivex.Single
import retrofit2.http.FieldMap
import anilibria.tv.api.impl.entity.common.ApiBaseResponse
import anilibria.tv.api.impl.entity.pagination.PaginatedResponse
import anilibria.tv.api.impl.entity.youtube.YouTubeResponse

interface YoutubeService {

    fun getList(@FieldMap fields: Map<String, String>): Single<ApiBaseResponse<PaginatedResponse<YouTubeResponse>>>
}