package tv.anilibria.feature.player.data.di

import toothpick.config.Module
import tv.anilibria.feature.player.data.EpisodeHistoryRepository
import tv.anilibria.feature.player.data.PlayerPreferencesStorage
import tv.anilibria.feature.player.data.local.EpisodeHistoryLocalDataSource
import tv.anilibria.plugin.data.storage.DataStorage

class PlayerDataFeatureModule : Module() {

    init {
        bind(DataStorage::class.java)
            .withName(PlayerPrefsStorageQualifier::class.java)
            .toProvider(PlayerPrefsDataStorageProvider::class.java)
            .providesSingleton()

        bind(DataStorage::class.java)
            .withName(EpisodeHistoryStorageQualifier::class.java)
            .toProvider(EpisodeHistoryDataStorageProvider::class.java)
            .providesSingleton()

        bind(PlayerPreferencesStorage::class.java).singleton()
        bind(EpisodeHistoryLocalDataSource::class.java).singleton()
        bind(EpisodeHistoryRepository::class.java).singleton()
    }
}