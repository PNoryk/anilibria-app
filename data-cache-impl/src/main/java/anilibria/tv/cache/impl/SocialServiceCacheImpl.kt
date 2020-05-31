package anilibria.tv.cache.impl

import anilibria.tv.cache.SocialServiceCache
import anilibria.tv.cache.impl.common.flatMapIfListEmpty
import anilibria.tv.cache.impl.memory.SocialServiceMemoryDataSource
import anilibria.tv.domain.entity.auth.SocialService
import anilibria.tv.storage.SocialServiceStorageDataSource
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import toothpick.InjectConstructor

@InjectConstructor
class SocialServiceCacheImpl(
    private val storageDataSource: SocialServiceStorageDataSource,
    private val memoryDataSource: SocialServiceMemoryDataSource
) : SocialServiceCache {

    override fun observeList(): Observable<List<SocialService>> = memoryDataSource.observeList()

    override fun getList(): Single<List<SocialService>> = memoryDataSource
        .getList()
        .flatMapIfListEmpty {
            storageDataSource
                .getList()
                .flatMapCompletable { memoryDataSource.insert(it) }
                .andThen(memoryDataSource.getList())
        }

    override fun putList(items: List<SocialService>): Completable = storageDataSource
        .putList(items)
        .andThen(storageDataSource.getList())
        .flatMapCompletable { memoryDataSource.insert(it) }
}