package anilibria.tv.cache.impl

import anilibria.tv.cache.YearCache
import anilibria.tv.cache.impl.common.flatMapIfListEmpty
import anilibria.tv.cache.impl.memory.YearMemoryDataSource
import anilibria.tv.storage.YearStorageDataSource
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import toothpick.InjectConstructor

@InjectConstructor
class YearCacheImpl(
    private val storageDataSource: YearStorageDataSource,
    private val memoryDataSource: YearMemoryDataSource
) : YearCache {

    override fun observeList(): Observable<List<String>> = memoryDataSource.observeList()

    override fun getList(): Single<List<String>> = memoryDataSource
        .getList()
        .flatMapIfListEmpty {
            storageDataSource
                .getList()
                .flatMapCompletable { memoryDataSource.insert(it) }
                .andThen(memoryDataSource.getList())
        }

    override fun putList(items: List<String>): Completable = storageDataSource
        .putList(items)
        .andThen(storageDataSource.getList())
        .flatMapCompletable { memoryDataSource.insert(it) }

    override fun clear(): Completable = storageDataSource
        .clear()
        .andThen(memoryDataSource.clear())
}