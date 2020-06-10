package anilibria.tv.cache.impl.combiner

import anilibria.tv.cache.EpisodeCache
import anilibria.tv.cache.EpisodeHistoryCache
import anilibria.tv.cache.combiner.EpisodeHistoryCacheCombiner
import anilibria.tv.domain.entity.common.keys.EpisodeKey
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
                .observeSome(relativeItems.toKeys())
                .map(getSourceCombiner(relativeItems))
        }

    override fun getList(): Single<List<EpisodeHistory>> = episodeHistoryCache
        .getList()
        .flatMap { relativeItems ->
            episodeCache
                .getSome(relativeItems.toKeys())
                .map(getSourceCombiner(relativeItems))
        }

    override fun putList(items: List<EpisodeHistory>): Completable {
        val putRelease = episodeCache.putList(items.map { it.episode })
        val putFavorite = episodeHistoryCache.putList(items.map { relativeConverter.toRelative(it) })
        return Completable.concat(listOf(putRelease, putFavorite))
    }

    override fun removeList(keys: List<EpisodeKey>): Completable = episodeHistoryCache.removeList(keys)

    override fun clear(): Completable = episodeHistoryCache.clear()

    private fun getSourceCombiner(relativeItems: List<EpisodeHistoryRelative>) =
        Function<List<Episode>, List<EpisodeHistory>> { episodeItems ->
            relativeItems.mapNotNull { relative ->
                relativeConverter.fromRelative(relative, episodeItems)
            }
        }

    private fun List<EpisodeHistoryRelative>.toKeys() = map { EpisodeKey(it.releaseId, it.id) }
}