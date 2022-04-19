package tv.anilibria.feature.menu.data.di

import toothpick.config.Module
import tv.anilibria.feature.menu.data.MenuRepository
import tv.anilibria.feature.menu.data.local.LinkMenuLocalDataSource
import tv.anilibria.feature.menu.data.remote.MenuApiWrapper
import tv.anilibria.feature.menu.data.remote.MenuRemoteDataSource

class MenuDataFeatureModule : Module() {

    init {
        bind(MenuApiWrapper::class.java)
        bind(MenuRemoteDataSource::class.java)
        bind(LinkMenuLocalDataSource::class.java)
        bind(MenuRepository::class.java)
    }
}