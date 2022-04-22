package tv.anilibria.feature.auth.data.di

import toothpick.config.Module
import tv.anilibria.feature.auth.data.AuthRepository
import tv.anilibria.feature.auth.data.AuthStateHolder
import tv.anilibria.feature.auth.data.AuthStateHolderImpl
import tv.anilibria.feature.auth.data.local.DeviceIdLocalDataSource
import tv.anilibria.feature.auth.data.local.SocialAuthLocalDataSource
import tv.anilibria.feature.auth.data.remote.AuthApiWrapper
import tv.anilibria.feature.auth.data.remote.AuthParser
import tv.anilibria.feature.auth.data.remote.AuthRemoteDataSource
import tv.anilibria.plugin.data.storage.DataStorage

class AuthDataFeatureModule : Module() {

    init {
        bind(DataStorage::class.java)
            .withName(AuthStorageQualifier::class.java)
            .toProvider(AuthDataStorageProvider::class.java)
            .providesSingleton()

        bind(DataStorage::class.java)
            .withName(SocialAuthStorageQualifier::class.java)
            .toProvider(SocialAuthDataStorageProvider::class.java)
            .providesSingleton()

        bind(AuthParser::class.java).singleton()
        bind(AuthApiWrapper::class.java).singleton()
        bind(AuthRemoteDataSource::class.java).singleton()
        bind(DeviceIdLocalDataSource::class.java).singleton()
        bind(SocialAuthLocalDataSource::class.java).singleton()
        bind(AuthRepository::class.java).singleton()
        bind(AuthStateHolder::class.java)
            .to(AuthStateHolderImpl::class.java)
            .singleton()
    }
}