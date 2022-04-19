package tv.anilibria.feature.content.data.remote.di

import toothpick.config.Module
import tv.anilibria.feature.content.data.remote.datasource.remote.api.*
import tv.anilibria.feature.content.data.remote.datasource.remote.retrofit.*

class ContentDataRemoteFeatureModule : Module() {

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
    }
}