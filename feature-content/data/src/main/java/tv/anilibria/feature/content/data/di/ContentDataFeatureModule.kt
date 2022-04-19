package tv.anilibria.feature.content.data.di

import toothpick.config.Module
import tv.anilibria.feature.content.data.ApiConfigPushHandler
import tv.anilibria.feature.content.data.ApiConfigPushHandlerImpl
import tv.anilibria.feature.content.data.BaseUrlHelper
import tv.anilibria.feature.content.data.ReleaseInteractor
import tv.anilibria.feature.content.data.local.ReleaseUpdateHelper
import tv.anilibria.feature.content.data.local.holders.GenresLocalDataSource
import tv.anilibria.feature.content.data.local.holders.ReleaseHistoryLocalDataSource
import tv.anilibria.feature.content.data.local.holders.ReleaseUpdatesLocalDataSource
import tv.anilibria.feature.content.data.local.holders.YearsLocalDataSource
import tv.anilibria.feature.content.data.remote.datasource.remote.api.*
import tv.anilibria.feature.content.data.remote.datasource.remote.retrofit.*
import tv.anilibria.feature.content.data.repos.*

class ContentDataFeatureModule : Module() {

    init {
        bind(FavoriteApiWrapper::class.java)
        bind(FeedApiWrapper::class.java)
        bind(ReleaseApiWrapper::class.java)
        bind(ScheduleApiWrapper::class.java)
        bind(SearchApiWrapper::class.java)
        bind(YoutubeApiWrapper::class.java)

        bind(FavoriteRemoteDataSource::class.java)
        bind(FeedRemoteDataSource::class.java)
        bind(ReleaseRemoteDataSource::class.java)
        bind(ScheduleRemoteDataSource::class.java)
        bind(SearchRemoteDataSource::class.java)
        bind(YoutubeRemoteDataSource::class.java)

        bind(GenresLocalDataSource::class.java)
        bind(ReleaseHistoryLocalDataSource::class.java)
        bind(ReleaseUpdatesLocalDataSource::class.java)
        bind(YearsLocalDataSource::class.java)

        bind(ReleaseUpdateHelper::class.java)

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
    }
}