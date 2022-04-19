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

class AuthDataFeatureModule : Module() {

    init {
        bind(AuthParser::class.java)
        bind(AuthApiWrapper::class.java)
        bind(AuthRemoteDataSource::class.java)
        bind(DeviceIdLocalDataSource::class.java)
        bind(SocialAuthLocalDataSource::class.java)
        bind(AuthRepository::class.java)
        bind(AuthStateHolder::class.java).to(AuthStateHolderImpl::class.java)
    }
}