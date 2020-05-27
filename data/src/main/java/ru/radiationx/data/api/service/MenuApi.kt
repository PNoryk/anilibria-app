package ru.radiationx.data.api.service

import io.reactivex.Single
import retrofit2.http.FieldMap
import ru.radiationx.data.api.entity.menu.LinkMenuResponse
import ru.radiationx.data.api.entity.common.ApiBaseResponse

interface MenuApi {

    fun getList(@FieldMap fields: Map<String, String>): Single<ApiBaseResponse<List<LinkMenuResponse>>>
}