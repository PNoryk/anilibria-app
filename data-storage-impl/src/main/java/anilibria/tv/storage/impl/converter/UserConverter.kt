package anilibria.tv.storage.impl.converter

import anilibria.tv.domain.entity.user.User
import anilibria.tv.storage.impl.entity.UserStorage
import toothpick.InjectConstructor

@InjectConstructor
class UserConverter {

    fun toDomain(source: UserStorage) = User(
        id = source.id,
        login = source.login,
        avatar = source.avatar
    )

    fun toStorage(source: User) = UserStorage(
        id = source.id,
        login = source.login,
        avatar = source.avatar
    )
}