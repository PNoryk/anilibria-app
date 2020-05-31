package ru.radiationx.data.aarepo

import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import anilibria.tv.cache.combiner.HistoryCacheCombiner
import anilibria.tv.domain.entity.history.HistoryItem
import anilibria.tv.domain.entity.release.Release
import toothpick.InjectConstructor
import java.util.*

@InjectConstructor
class HistoryRepository(
    private val cacheCombiner: HistoryCacheCombiner
) {

    fun observeList(): Observable<List<HistoryItem>> = cacheCombiner.observeList()

    fun getList(): Single<List<HistoryItem>> = cacheCombiner.getList()

    fun visitRelease(item: Release): Completable = cacheCombiner.putList(listOf(
        HistoryItem(Date(), item)
    ))
}