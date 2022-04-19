package tv.anilibria.feature.user.data.di

import toothpick.config.Module
import tv.anilibria.feature.user.data.UserRepository
import tv.anilibria.feature.user.data.local.UserLocalDataSource
import tv.anilibria.feature.user.data.remote.UserApiWrapper
import tv.anilibria.feature.user.data.remote.UserRemoteDataSource
import tv.anilibria.plugin.data.storage.DataStorage

class UserDataFeatureModule : Module() {

    init {
        bind(DataStorage::class.java)
            .withName(UserStorageQualifier::class.java)
            .toProvider(UserDataStorageProvider::class.java)
            .providesSingleton()
        bind(UserApiWrapper::class.java)
        bind(UserLocalDataSource::class.java)
        bind(UserRemoteDataSource::class.java)
        bind(UserRepository::class.java)
    }
}