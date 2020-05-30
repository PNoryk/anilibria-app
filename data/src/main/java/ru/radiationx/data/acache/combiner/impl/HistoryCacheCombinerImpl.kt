package ru.radiationx.data.acache.combiner.impl

import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.functions.Function
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

    override fun observeList(): Observable<List<HistoryItem>> = historyCache
        .observeList()
        .switchMap { relativeItems ->
            releaseCache
                .observeList(relativeItems.map { it.releaseId })
                .map(getSourceCombiner(relativeItems))
        }

    override fun getList(): Single<List<HistoryItem>> = historyCache
        .getList()
        .flatMap { relativeItems ->
            releaseCache
                .getList(relativeItems.map { it.releaseId })
                .map(getSourceCombiner(relativeItems))
        }

    override fun putList(items: List<HistoryItem>): Completable {
        val putRelease = releaseCache.putList(items.map { it.release })
        val putFavorite = historyCache.putList(items.map { HistoryRelative(it.release.id, it.timestamp) })
        return Completable.concat(listOf(putRelease, putFavorite))
    }

    override fun removeList(items: List<HistoryItem>): Completable = historyCache
        .removeList(items.map { HistoryRelative(it.release.id, it.timestamp) })

    override fun clear(): Completable = historyCache.clear()

    private fun getSourceCombiner(relativeItems: List<HistoryRelative>) = Function<List<Release>, List<HistoryItem>> { releaseItems ->
        relativeItems.mapNotNull { relative ->
            val release = releaseItems.firstOrNull { it.id == relative.releaseId }
            release?.let { HistoryItem(relative.timestamp, release) }
        }
    }
}