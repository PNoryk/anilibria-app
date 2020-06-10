package ru.radiationx.data.aarepo

import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import anilibria.tv.cache.combiner.ReleaseHistoryCacheCombiner
import anilibria.tv.domain.entity.history.ReleaseHistory
import anilibria.tv.domain.entity.release.Release
import toothpick.InjectConstructor
import java.util.*

@InjectConstructor
class HistoryRepository(
    private val cacheCombinerRelease: ReleaseHistoryCacheCombiner
) {

    fun observeList(): Observable<List<ReleaseHistory>> = cacheCombinerRelease.observeList()

    fun getList(): Single<List<ReleaseHistory>> = cacheCombinerRelease.getList()

    fun visitRelease(item: Release): Completable = cacheCombinerRelease.insert(listOf(
        ReleaseHistory(Date(), item)
    ))
}