package tv.anilibria.feature.player.data.di

import toothpick.config.Module
import tv.anilibria.feature.player.data.EpisodeHistoryRepository
import tv.anilibria.feature.player.data.PlayerPreferencesStorage
import tv.anilibria.feature.player.data.local.EpisodeHistoryLocalDataSource

class PlayerDataFeatureModule : Module() {

    init {
        bind(PlayerPreferencesStorage::class.java)
        bind(EpisodeHistoryLocalDataSource::class.java)
        bind(EpisodeHistoryRepository::class.java)
    }
}