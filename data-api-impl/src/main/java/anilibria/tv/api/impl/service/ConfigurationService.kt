package anilibria.tv.api.impl.service

import io.reactivex.Single
import retrofit2.http.FieldMap
import anilibria.tv.api.impl.entity.common.ApiBaseResponse
import anilibria.tv.api.impl.entity.config.ConfigResponse

interface ConfigurationService {

    fun get(@FieldMap fields: Map<String, String>): Single<ApiBaseResponse<ConfigResponse>>
}