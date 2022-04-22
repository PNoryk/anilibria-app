package tv.anilibria.feature.appupdates.data.di

import toothpick.config.Module
import tv.anilibria.feature.appupdates.data.CheckerRepository
import tv.anilibria.feature.appupdates.data.UpdaterApiWrapper
import tv.anilibria.feature.appupdates.data.UpdatesRemoteDataSource

class AppUpdatesDataFeatureModule : Module() {

    init {
        bind(UpdaterApiWrapper::class.java).singleton()
        bind(UpdatesRemoteDataSource::class.java).singleton()
        bind(CheckerRepository::class.java).singleton()
    }
}