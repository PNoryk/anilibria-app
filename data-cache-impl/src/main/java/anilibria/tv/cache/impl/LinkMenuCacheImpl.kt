package anilibria.tv.cache.impl

import anilibria.tv.cache.LinkMenuCache
import anilibria.tv.cache.impl.common.flatMapIfListEmpty
import anilibria.tv.cache.impl.memory.LinkMenuMemoryDataSource
import anilibria.tv.domain.entity.menu.LinkMenu
import anilibria.tv.storage.LinkMenuStorageDataSource
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import toothpick.InjectConstructor

@InjectConstructor
class LinkMenuCacheImpl(
    private val storageDataSource: LinkMenuStorageDataSource,
    private val memoryDataSource: LinkMenuMemoryDataSource
) : LinkMenuCache {

    override fun observeList(): Observable<List<LinkMenu>> = memoryDataSource.observeList()

    override fun getList(): Single<List<LinkMenu>> = memoryDataSource
        .getList()
        .flatMapIfListEmpty {
            storageDataSource
                .getList()
                .flatMapCompletable { memoryDataSource.insert(it) }
                .andThen(memoryDataSource.getList())
        }

    override fun putList(items: List<LinkMenu>): Completable = storageDataSource
        .putList(items)
        .andThen(storageDataSource.getList())
        .flatMapCompletable { memoryDataSource.insert(it) }
}