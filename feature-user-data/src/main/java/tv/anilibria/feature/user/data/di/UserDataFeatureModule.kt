package tv.anilibria.feature.user.data.di

import toothpick.config.Module
import toothpick.ktp.binding.bind
import tv.anilibria.feature.user.data.UserRepository
import tv.anilibria.feature.user.data.local.UserLocalDataSource
import tv.anilibria.feature.user.data.remote.UserApiWrapper
import tv.anilibria.feature.user.data.remote.UserRemoteDataSource

class UserDataFeatureModule : Module() {

    init {
        bind<UserApiWrapper>()
        bind<UserLocalDataSource>()
        bind<UserRemoteDataSource>()
        bind<UserRepository>()
    }
}