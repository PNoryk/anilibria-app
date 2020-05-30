package ru.radiationx.data.acache.combiner.impl

import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.functions.BiFunction
import ru.radiationx.data.acache.HistoryCache
import ru.radiationx.data.acache.combiner.HistoryCacheCombiner
import ru.radiationx.data.acache.combiner.ReleaseCacheCombiner
import ru.radiationx.data.adomain.entity.history.HistoryItem
import ru.radiationx.data.adomain.entity.relative.HistoryRelative
import ru.radiationx.data.adomain.entity.release.Release
import toothpick.InjectConstructor

@InjectConstructor
class HistoryCacheCombinerImpl(
    private val historyCache: HistoryCache,
    private val releaseCache: ReleaseCacheCombiner
) : HistoryCacheCombiner {

    private val combiner by lazy {
        BiFunction<List<HistoryRelative>, List<Release>, List<HistoryItem>> { relativeItems, releaseItems ->
            relativeItems.mapNotNull { relative ->
                val release = releaseItems.firstOrNull { it.id == relative.releaseId }
                release?.let { HistoryItem(relative.timestamp, release) }
            }
        }
    }

    override fun observeList(): Observable<List<HistoryItem>> = Observable
        .combineLatest(
            historyCache.observeList(),
            releaseCache.observeList(),
            combiner
        )

    override fun getList(): Single<List<HistoryItem>> = Single.zip(
        historyCache.getList(),
        releaseCache.getList(),
        combiner
    )

    override fun putList(items: List<HistoryItem>): Completable {
        val putRelease = releaseCache.putList(items.map { it.release })
        val putFavorite = historyCache.putList(items.map { HistoryRelative(it.release.id, it.timestamp) })
        return Completable.concat(listOf(putRelease, putFavorite))
    }

    override fun removeList(items: List<HistoryItem>): Completable = historyCache
        .removeList(items.map { HistoryRelative(it.release.id, it.timestamp) })

    override fun clear(): Completable = historyCache.clear()

}