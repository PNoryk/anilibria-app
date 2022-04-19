package tv.anilibria.feature.networkconfig.data.di

import toothpick.config.Module
import tv.anilibria.feature.networkconfig.data.*
import tv.anilibria.plugin.data.storage.DataStorage

class NetworkConfigDataFeatureModule : Module() {

    init {
        bind(DataStorage::class.java)
            .withName(ConfigStorageQualifier::class.java)
            .toProvider(ConfigDataStorageProvider::class.java)
            .providesSingleton()

        bind(ConfigPingCache::class.java)
        bind(ConfigApiWrapper::class.java)
        bind(ConfigLocalDataStorage::class.java)
        bind(ConfigRemoteDataSource::class.java)
        bind(ConfigurationRepository::class.java)
        bind(ConfiguringInteractor::class.java)
        bind(ConfiguringAnalytics::class.java)
    }
}