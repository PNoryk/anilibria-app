package tv.anilibria.feature.menu.data

import kotlinx.coroutines.flow.Flow
import toothpick.InjectConstructor
import tv.anilibria.feature.menu.data.domain.LinkMenuItem
import tv.anilibria.feature.menu.data.local.LinkMenuLocalDataSource
import tv.anilibria.feature.menu.data.remote.MenuRemoteDataSource

@InjectConstructor
class MenuRepository(
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