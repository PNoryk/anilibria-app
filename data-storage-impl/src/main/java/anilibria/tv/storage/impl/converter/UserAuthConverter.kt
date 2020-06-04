package anilibria.tv.storage.impl.converter

import anilibria.tv.domain.entity.auth.UserAuth
import anilibria.tv.storage.impl.entity.UserAuthStorage
import toothpick.InjectConstructor

@InjectConstructor
class UserAuthConverter(
    private val userConverter: UserConverter
) {

    fun toDomain(source: UserAuthStorage) = UserAuth(
        state = source.state.asDomainState(),
        user = source.user?.let { userConverter.toDomain(it) }
    )

    fun toStorage(source: UserAuth) = UserAuthStorage(
        state = source.state.asStorageState(),
        user = source.user?.let { userConverter.toStorage(it) }
    )

    private fun UserAuth.State.asStorageState(): Int = when (this) {
        UserAuth.State.NO_AUTH -> UserAuthStorage.STATE_NO_AUTH
        UserAuth.State.AUTH -> UserAuthStorage.STATE_AUTH
    }

    private fun Int.asDomainState(): UserAuth.State = when (this) {
        UserAuthStorage.STATE_NO_AUTH -> UserAuth.State.NO_AUTH
        UserAuthStorage.STATE_AUTH -> UserAuth.State.AUTH
        else -> throw IllegalStateException("Wrong auth state")
    }
}