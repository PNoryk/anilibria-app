package ru.radiationx.data.api.service.menu

import io.reactivex.Single
import retrofit2.http.FieldMap
import ru.radiationx.data.api.remote.LinkMenuResponse
import ru.radiationx.data.api.common.ApiBaseResponse

interface MenuApi {

    fun getList(@FieldMap fields: Map<String, String>): Single<ApiBaseResponse<List<LinkMenuResponse>>>
}