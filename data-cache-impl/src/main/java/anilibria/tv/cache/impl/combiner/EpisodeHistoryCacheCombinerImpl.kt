package anilibria.tv.cache.impl.combiner

import anilibria.tv.cache.EpisodeCache
import anilibria.tv.cache.EpisodeHistoryCache
import anilibria.tv.cache.combiner.EpisodeHistoryCacheCombiner
import anilibria.tv.domain.entity.converter.EpisodeHistoryRelativeConverter
import anilibria.tv.domain.entity.episode.Episode
import anilibria.tv.domain.entity.history.EpisodeHistory
import anilibria.tv.domain.entity.relative.EpisodeHistoryRelative
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.functions.Function
import toothpick.InjectConstructor

@InjectConstructor
class EpisodeHistoryCacheCombinerImpl(
    private val episodeHistoryCache: EpisodeHistoryCache,
    private val episodeCache: EpisodeCache,
    private val relativeConverter: EpisodeHistoryRelativeConverter
) : EpisodeHistoryCacheCombiner {

    override fun observeList(): Observable<List<EpisodeHistory>> = episodeHistoryCache
        .observeList()
        .switchMap { relativeItems ->
            episodeCache
                .observeList(relativeItems.map { it.releaseId })
                .map(getSourceCombiner(relativeItems))
        }

    override fun getList(): Single<List<EpisodeHistory>> = episodeHistoryCache
        .getList()
        .flatMap { relativeItems ->
            episodeCache
                .getList(relativeItems.map { it.releaseId })
                .map(getSourceCombiner(relativeItems))
        }

    override fun putList(items: List<EpisodeHistory>): Completable {
        val putRelease = episodeCache.putList(items.map { it.episode })
        val putFavorite = episodeHistoryCache.putList(items.map { relativeConverter.toRelative(it) })
        return Completable.concat(listOf(putRelease, putFavorite))
    }

    override fun removeList(items: List<EpisodeHistory>): Completable = episodeHistoryCache
        .removeList(items.map { relativeConverter.toRelative(it) })

    override fun clear(): Completable = episodeHistoryCache.clear()

    private fun getSourceCombiner(relativeItems: List<EpisodeHistoryRelative>) =
        Function<List<Episode>, List<EpisodeHistory>> { episodeItems ->
            relativeItems.mapNotNull { relative ->
                relativeConverter.fromRelative(relative, episodeItems)
            }
        }
}