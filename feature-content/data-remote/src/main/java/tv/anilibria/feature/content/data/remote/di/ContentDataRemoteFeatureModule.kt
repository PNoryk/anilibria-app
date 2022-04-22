package tv.anilibria.feature.content.data.remote.di

import toothpick.config.Module
import tv.anilibria.feature.content.data.remote.datasource.remote.api.*
import tv.anilibria.feature.content.data.remote.datasource.remote.retrofit.*

class ContentDataRemoteFeatureModule : Module() {

    init {
        bind(FavoriteApiWrapper::class.java).singleton()
        bind(FeedApiWrapper::class.java).singleton()
        bind(ReleaseApiWrapper::class.java).singleton()
        bind(ScheduleApiWrapper::class.java).singleton()
        bind(SearchApiWrapper::class.java).singleton()
        bind(YoutubeApiWrapper::class.java).singleton()

        bind(FavoriteRemoteDataSource::class.java).singleton()
        bind(FeedRemoteDataSource::class.java).singleton()
        bind(ReleaseRemoteDataSource::class.java).singleton()
        bind(ScheduleRemoteDataSource::class.java).singleton()
        bind(SearchRemoteDataSource::class.java).singleton()
        bind(YoutubeRemoteDataSource::class.java).singleton()
    }
}