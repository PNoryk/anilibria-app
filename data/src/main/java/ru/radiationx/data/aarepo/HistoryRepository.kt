package ru.radiationx.data.aarepo

import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import ru.radiationx.data.acache.combiner.HistoryCacheCombiner
import ru.radiationx.data.adomain.entity.feed.Feed
import ru.radiationx.data.adomain.entity.history.HistoryItem
import ru.radiationx.data.adomain.entity.release.Release
import toothpick.InjectConstructor
import java.util.*

@InjectConstructor
class HistoryRepository(
    private val cacheCombiner: HistoryCacheCombiner
) {

    fun observeList(): Observable<List<HistoryItem>> = cacheCombiner.observeList()

    fun getList(): Single<List<HistoryItem>> = cacheCombiner.fetchList()

    fun visitRelease(item: Release): Completable = cacheCombiner.putOne(HistoryItem(Date(), item))
}