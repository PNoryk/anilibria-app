package anilibria.tv.storage.impl

import anilibria.tv.domain.entity.auth.UserAuth
import anilibria.tv.domain.entity.user.User
import anilibria.tv.storage.UserAuthStorageDataSource
import anilibria.tv.storage.common.KeyValueStorage
import anilibria.tv.storage.impl.converter.UserAuthConverter
import anilibria.tv.storage.impl.entity.UserAuthStorage
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.reactivex.Completable
import io.reactivex.Single
import toothpick.InjectConstructor

@InjectConstructor
class UserAuthStorageDataSourceImpl(
    private val keyValueStorage: KeyValueStorage,
    private val gson: Gson,
    private val converter: UserAuthConverter
) : UserAuthStorageDataSource {

    companion object {
        private const val KEY = "saved_user"

        private val dataType = object : TypeToken<UserAuthStorage>() {}.type

        private val defaultUser = UserAuth(
            state = UserAuth.State.NO_AUTH,
            user = null
        )
    }

    override fun getUser(): Single<UserAuth> = keyValueStorage
        .getValue(KEY)
        .map { gson.fromJson<UserAuthStorage>(it, dataType) }
        .map { converter.toDomain(it) }
        .switchIfEmpty(Single.just(defaultUser))

    override fun putUser(user: User): Completable = Single
        .fromCallable { converter.toStorage(UserAuth(state = UserAuth.State.AUTH, user = user)) }
        .map { gson.toJson(it, dataType) }
        .flatMapCompletable { keyValueStorage.putValue(KEY, it) }

    override fun delete(): Completable = keyValueStorage.delete(KEY)
}