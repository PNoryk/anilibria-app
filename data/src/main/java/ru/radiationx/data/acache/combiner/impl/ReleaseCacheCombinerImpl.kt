package ru.radiationx.data.acache.combiner.impl

import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.functions.BiFunction
import io.reactivex.functions.Function3
import ru.radiationx.data.acache.EpisodeCache
import ru.radiationx.data.acache.ReleaseCache
import ru.radiationx.data.acache.TorrentCache
import ru.radiationx.data.acache.combiner.ReleaseCacheCombiner
import ru.radiationx.data.adomain.entity.release.Episode
import ru.radiationx.data.adomain.entity.release.Release
import ru.radiationx.data.adomain.entity.release.Torrent
import toothpick.InjectConstructor

@InjectConstructor
class ReleaseCacheCombinerImpl(
    private val releaseCache: ReleaseCache,
    private val episodeCache: EpisodeCache,
    private val torrentCache: TorrentCache
) : ReleaseCacheCombiner {

    private val combiner by lazy {
        Function3<List<Release>, List<Episode>, List<Torrent>, List<Release>> { releaseItems, episodeItems, torrentItems ->
            releaseItems.map { release ->
                val episodes = episodeItems.filter { it.releaseId == release.id }
                val torrents = torrentItems.filter { it.releaseId == release.id }
                release.copy(playlist = episodes, torrents = torrents)
            }
        }
    }

    override fun observeList(): Observable<List<Release>> = Observable
        .combineLatest(
            releaseCache.observeList(),
            episodeCache.observeList(),
            torrentCache.observeList(),
            combiner
        )

    override fun fetchList(): Single<List<Release>> = Single.zip(
        releaseCache.fetchList(),
        episodeCache.fetchList(),
        torrentCache.fetchList(),
        combiner
    )

    override fun putList(items: List<Release>): Completable {
        val putEpisodes = episodeCache.putList(items.mapNotNull { it.playlist }.flatten())
        val putTorrents = torrentCache.putList(items.mapNotNull { it.torrents }.flatten())
        val putRelease = releaseCache.putList(items)
        return Completable.concat(listOf(putEpisodes, putTorrents, putRelease))
    }

    override fun removeList(items: List<Release>): Completable = releaseCache
        .removeList(items)

    override fun clear(): Completable = releaseCache.clear()

}