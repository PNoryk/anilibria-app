package tv.anilibria.feature.networkconfig.data.di

import toothpick.config.Module
import tv.anilibria.feature.networkconfig.data.*
import tv.anilibria.feature.networkconfig.data.address.ApiConfigChanger
import tv.anilibria.feature.networkconfig.data.address.ApiConfigController
import tv.anilibria.plugin.data.storage.DataStorage

class NetworkConfigDataFeatureModule : Module() {

    init {
        bind(DataStorage::class.java)
            .withName(ConfigStorageQualifier::class.java)
            .toProvider(ConfigDataStorageProvider::class.java)
            .providesSingleton()

        bind(ConfigPingCache::class.java).singleton()
        bind(ConfigApiWrapper::class.java).singleton()
        bind(ConfigLocalDataStorage::class.java).singleton()
        bind(ConfigRemoteDataSource::class.java).singleton()
        bind(ConfigurationRepository::class.java).singleton()
        bind(ConfiguringInteractor::class.java).singleton()
        bind(ConfiguringAnalytics::class.java).singleton()

        bind(ApiConfigChanger::class.java).singleton()
        bind(ApiConfigController::class.java).singleton()
    }
}