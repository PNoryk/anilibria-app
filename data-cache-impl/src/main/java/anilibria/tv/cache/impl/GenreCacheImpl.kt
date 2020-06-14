package anilibria.tv.cache.impl

import anilibria.tv.cache.GenreCache
import anilibria.tv.cache.impl.common.flatMapIfListEmpty
import anilibria.tv.cache.impl.memory.GenreMemoryDataSource
import anilibria.tv.storage.GenreStorageDataSource
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import toothpick.InjectConstructor

@InjectConstructor
class GenreCacheImpl(
    private val storageDataSource: GenreStorageDataSource,
    private val memoryDataSource: GenreMemoryDataSource
) : GenreCache {

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