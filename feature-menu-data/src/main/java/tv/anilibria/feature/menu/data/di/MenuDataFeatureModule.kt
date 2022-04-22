package tv.anilibria.feature.menu.data.di

import toothpick.config.Module
import tv.anilibria.feature.menu.data.MenuRepository
import tv.anilibria.feature.menu.data.local.LinkMenuLocalDataSource
import tv.anilibria.feature.menu.data.remote.MenuApiWrapper
import tv.anilibria.feature.menu.data.remote.MenuRemoteDataSource
import tv.anilibria.plugin.data.storage.DataStorage

class MenuDataFeatureModule : Module() {

    init {
        bind(DataStorage::class.java)
            .withName(MenuStorageQualifier::class.java)
            .toProvider(MenuDataStorageProvider::class.java)
            .providesSingleton()

        bind(MenuApiWrapper::class.java).singleton()
        bind(MenuRemoteDataSource::class.java).singleton()
        bind(LinkMenuLocalDataSource::class.java).singleton()
        bind(MenuRepository::class.java).singleton()
    }
}