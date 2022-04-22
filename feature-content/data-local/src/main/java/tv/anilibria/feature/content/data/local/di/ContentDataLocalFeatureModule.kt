package tv.anilibria.feature.content.data.local.di

import toothpick.config.Module
import tv.anilibria.feature.content.data.local.ReleaseUpdateHelper
import tv.anilibria.feature.content.data.local.holders.GenresLocalDataSource
import tv.anilibria.feature.content.data.local.holders.ReleaseHistoryLocalDataSource
import tv.anilibria.feature.content.data.local.holders.ReleaseUpdatesLocalDataSource
import tv.anilibria.feature.content.data.local.holders.YearsLocalDataSource
import tv.anilibria.plugin.data.storage.DataStorage

class ContentDataLocalFeatureModule : Module() {

    init {
        bind(DataStorage::class.java)
            .withName(GenresStorageQualifier::class.java)
            .toProvider(GenresDataStorageProvider::class.java)
            .providesSingleton()
        bind(DataStorage::class.java)
            .withName(ReleaseHistoryStorageQualifier::class.java)
            .toProvider(ReleaseHistoryDataStorageProvider::class.java)
            .providesSingleton()
        bind(DataStorage::class.java)
            .withName(ReleaseUpdatesStorageQualifier::class.java)
            .toProvider(ReleaseUpdatesDataStorageProvider::class.java)
            .providesSingleton()
        bind(DataStorage::class.java)
            .withName(YearsStorageQualifier::class.java)
            .toProvider(YearsDataStorageProvider::class.java)
            .providesSingleton()

        bind(GenresLocalDataSource::class.java).singleton()
        bind(ReleaseHistoryLocalDataSource::class.java).singleton()
        bind(ReleaseUpdatesLocalDataSource::class.java).singleton()
        bind(YearsLocalDataSource::class.java).singleton()

        bind(ReleaseUpdateHelper::class.java).singleton()
    }
}