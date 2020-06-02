package anilibria.tv.cache.impl

import anilibria.tv.cache.UserAuthCache
import anilibria.tv.cache.impl.memory.UserAuthMemoryDataSource
import anilibria.tv.domain.entity.auth.UserAuth
import anilibria.tv.domain.entity.user.User
import anilibria.tv.storage.UserAuthStorageDataSource
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import toothpick.InjectConstructor

@InjectConstructor
class UserAuthCacheImpl(
    private val storageDataSource: UserAuthStorageDataSource,
    private val memoryDataSource: UserAuthMemoryDataSource
) : UserAuthCache {

    override fun observeUser(): Observable<UserAuth> = memoryDataSource.observeData()

    override fun getUser(): Single<UserAuth> = memoryDataSource
        .getData()
        .switchIfEmpty(getAndUpdate())

    override fun putUser(user: User): Completable = storageDataSource
        .putUser(user)
        .andThen(storageDataSource.getUser())
        .flatMapCompletable { memoryDataSource.insert(it) }

    override fun deleteUser(): Completable = storageDataSource
        .delete()
        .andThen(storageDataSource.getUser())
        .flatMapCompletable { memoryDataSource.insert(it) }

    private fun getAndUpdate(): Single<UserAuth> = storageDataSource
        .getUser()
        .flatMapCompletable { memoryDataSource.insert(it) }
        .andThen(memoryDataSource.getData().toSingle())
}