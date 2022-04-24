package tv.anilibria.feature.content.data.di

import toothpick.config.Module
import tv.anilibria.feature.content.data.ApiConfigPushHandler
import tv.anilibria.feature.content.data.ApiConfigPushHandlerImpl
import tv.anilibria.feature.content.data.BaseUrlHelper
import tv.anilibria.feature.content.data.repos.ReleaseCacheRepository
import tv.anilibria.feature.content.data.migration.MigrationDataSource
import tv.anilibria.feature.content.data.migration.MigrationDataSourceImpl
import tv.anilibria.feature.content.data.repos.*
import tv.anilibria.plugin.data.storage.DataStorage

class ContentDataFeatureModule : Module() {

    init {
        bind(FavoriteRepository::class.java).singleton()
        bind(FeedRepository::class.java).singleton()
        bind(HistoryRepository::class.java).singleton()
        bind(ReleaseRepository::class.java).singleton()
        bind(ScheduleRepository::class.java).singleton()
        bind(SearchRepository::class.java).singleton()
        bind(YoutubeRepository::class.java).singleton()
        bind(ReleaseCacheRepository::class.java).singleton()

        bind(ApiConfigPushHandler::class.java)
            .to(ApiConfigPushHandlerImpl::class.java)
            .singleton()

        bind(BaseUrlHelper::class.java).singleton()

        bind(DataStorage::class.java)
            .withName(MigrationStorageQualifier::class.java)
            .toProvider(MigrationDataStorageProvider::class.java)
            .providesSingleton()
        bind(MigrationDataSource::class.java)
            .to(MigrationDataSourceImpl::class.java)
            .singleton()
    }
}