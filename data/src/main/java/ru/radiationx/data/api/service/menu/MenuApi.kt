package ru.radiationx.data.api.service.menu

import io.reactivex.Single
import ru.radiationx.data.api.remote.LinkMenuResponse
import ru.radiationx.data.api.remote.common.ApiBaseResponse

interface MenuApi {

    fun getList(): Single<ApiBaseResponse<List<LinkMenuResponse>>>
}