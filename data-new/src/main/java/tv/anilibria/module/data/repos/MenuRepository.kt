package tv.anilibria.module.data.repos

import kotlinx.coroutines.flow.Flow
import tv.anilibria.module.data.local.holders.LinkMenuLocalDataSource
import tv.anilibria.module.data.restapi.datasource.remote.api.MenuRemoteDataSource
import tv.anilibria.module.domain.entity.other.LinkMenuItem
import javax.inject.Inject

class MenuRepository @Inject constructor(
    private val menuApi: MenuRemoteDataSource,
    private val menuHolder: LinkMenuLocalDataSource
) {

    fun observeMenu(): Flow<List<LinkMenuItem>> {
        return menuHolder.observe()
    }

    suspend fun getMenu(): List<LinkMenuItem> {
        return menuApi.getMenu().also {
            menuHolder.put(it)
        }
    }
}