package tv.anilibria.feature.page.data.di

import toothpick.config.Module
import tv.anilibria.feature.page.data.PageRepository
import tv.anilibria.feature.page.data.remote.PageApiWrapper
import tv.anilibria.feature.page.data.remote.PageRemoteDataSource

class PageDataFeatureModule : Module() {

    init {
        bind(PageApiWrapper::class.java).singleton()
        bind(PageRemoteDataSource::class.java).singleton()
        bind(PageRepository::class.java).singleton()
    }
}