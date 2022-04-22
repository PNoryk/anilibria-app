package tv.anilibria.feature.content.data.di

import toothpick.config.Module
import tv.anilibria.feature.content.data.ApiConfigPushHandler
import tv.anilibria.feature.content.data.ApiConfigPushHandlerImpl
import tv.anilibria.feature.content.data.BaseUrlHelper
import tv.anilibria.feature.content.data.ReleaseInteractor
import tv.anilibria.feature.content.data.migration.MigrationDataSource
import tv.anilibria.feature.content.data.migration.MigrationDataSourceImpl
import tv.anilibria.feature.content.data.repos.*
import tv.anilibria.plugin.data.storage.DataStorage

class ContentDataFeatureModule : Module() {

    init {
        bind(FavoriteRepository::class.java)
        bind(FeedRepository::class.java)
        bind(HistoryRepository::class.java)
        bind(ReleaseRepository::class.java)
        bind(ScheduleRepository::class.java)
        bind(SearchRepository::class.java)
        bind(YoutubeRepository::class.java)
        bind(ReleaseInteractor::class.java)

        bind(ApiConfigPushHandler::class.java).to(ApiConfigPushHandlerImpl::class.java)

        bind(BaseUrlHelper::class.java)

        bind(DataStorage::class.java)
            .withName(MigrationStorageQualifier::class.java)
            .toProvider(MigrationDataStorageProvider::class.java)
            .providesSingleton()
        bind(MigrationDataSource::class.java).to(MigrationDataSourceImpl::class.java).singleton()
    }
}