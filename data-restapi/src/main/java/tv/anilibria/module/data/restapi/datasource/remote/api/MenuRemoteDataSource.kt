package tv.anilibria.module.data.restapi.datasource.remote.api

import tv.anilibria.module.data.restapi.datasource.remote.retrofit.OtherApi
import tv.anilibria.module.data.restapi.entity.mapper.toDomain
import tv.anilibria.module.domain.entity.other.LinkMenuItem
import tv.anilibria.plugin.data.network.formBodyOf
import tv.anilibria.plugin.data.restapi.ApiWrapper
import tv.anilibria.plugin.data.restapi.handleApiResponse
import javax.inject.Inject

class MenuRemoteDataSource @Inject constructor(
    private val otherApi: ApiWrapper<OtherApi>
) {

    suspend fun getMenu(): List<LinkMenuItem> {
        val args = formBodyOf(
            "query" to "link_menu"
        )
        return otherApi.proxy()
            .getMenu(args)
            .handleApiResponse()
            .map { it.toDomain() }
    }

}