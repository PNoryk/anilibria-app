package tv.anilibria.module.data.restapi.datasource.remote.api

import io.reactivex.Single
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

    fun getMenu(): Single<List<LinkMenuItem>> {
        val args = formBodyOf(
            "query" to "link_menu"
        )
        return otherApi.proxy()
            .getMenu(args)
            .handleApiResponse()
            .map { items -> items.map { it.toDomain() } }
    }

}