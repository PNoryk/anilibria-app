package anilibria.tv.cache.impl.combiner

import anilibria.tv.cache.EpisodeCache
import anilibria.tv.cache.ReleaseCache
import anilibria.tv.cache.TorrentCache
import anilibria.tv.cache.combiner.ReleaseCacheCombiner
import anilibria.tv.domain.entity.common.keys.EpisodeKey
import anilibria.tv.domain.entity.common.keys.ReleaseKey
import anilibria.tv.domain.entity.common.keys.TorrentKey
import anilibria.tv.domain.entity.episode.Episode
import anilibria.tv.domain.entity.release.Release
import anilibria.tv.domain.entity.torrent.Torrent
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.functions.BiFunction
import toothpick.InjectConstructor

@InjectConstructor
class ReleaseCacheCombinerImpl(
    private val releaseCache: ReleaseCache,
    private val episodeCache: EpisodeCache,
    private val torrentCache: TorrentCache
) : ReleaseCacheCombiner {

    override fun observeList(): Observable<List<Release>> = releaseCache
        .observeList()
        .switchMap { releaseItems ->
            val episodeKeys = releaseItems.toEpisodeKeys()
            val torrentKeys = releaseItems.toTorrentKeys()
            Observable.zip(
                episodeCache.observeSome(episodeKeys),
                torrentCache.observeSome(torrentKeys),
                getSourceCombiner(releaseItems)
            )
        }

    override fun observeSome(keys: List<ReleaseKey>): Observable<List<Release>> = releaseCache
        .observeList()
        .switchMap { releaseItems ->
            val episodeKeys = releaseItems.toEpisodeKeys()
            val torrentKeys = releaseItems.toTorrentKeys()
            Observable.zip(
                episodeCache.observeSome(episodeKeys),
                torrentCache.observeSome(torrentKeys),
                getSourceCombiner(releaseItems)
            )
        }

    override fun observeOne(key: ReleaseKey): Observable<Release> = releaseCache
        .observeOne(key)
        .switchMap { relativeItem ->
            val episodeKeys = listOf(relativeItem.toEpisodeKey())
            val torrentKeys = listOf(relativeItem.toTorrentKey())
            Observable.zip(
                episodeCache.observeSome(episodeKeys),
                torrentCache.observeSome(torrentKeys),
                getSourceCombiner(relativeItem)
            )
        }

    override fun getList(): Single<List<Release>> = releaseCache
        .getList()
        .flatMap { releaseItems ->
            val episodeKeys = releaseItems.toEpisodeKeys()
            val torrentKeys = releaseItems.toTorrentKeys()
            Single.zip(
                episodeCache.getSome(episodeKeys),
                torrentCache.getSome(torrentKeys),
                getSourceCombiner(releaseItems)
            )
        }

    override fun getSome(keys: List<ReleaseKey>): Single<List<Release>> = releaseCache
        .getSome(keys)
        .flatMap { releaseItems ->
            val episodeKeys = releaseItems.toEpisodeKeys()
            val torrentKeys = releaseItems.toTorrentKeys()
            Single.zip(
                episodeCache.getSome(episodeKeys),
                torrentCache.getSome(torrentKeys),
                getSourceCombiner(releaseItems)
            )
        }

    override fun getOne(key: ReleaseKey): Single<Release> = releaseCache
        .getOne(key)
        .flatMap { relativeItem ->
            val episodeKeys = listOf(relativeItem.toEpisodeKey())
            val torrentKeys = listOf(relativeItem.toTorrentKey())
            Single.zip(
                episodeCache.getSome(episodeKeys),
                torrentCache.getSome(torrentKeys),
                getSourceCombiner(relativeItem)
            )
        }

    override fun insert(items: List<Release>): Completable = Completable.defer {
        val putEpisodes = episodeCache.insert(items.mapNotNull { it.playlist }.flatten())
        val putTorrents = torrentCache.insert(items.mapNotNull { it.torrents }.flatten())
        val putRelease = releaseCache.insert(items.map { it.copy(playlist = null, torrents = null) })
        Completable.concat(listOf(putEpisodes, putTorrents, putRelease))
    }

    override fun remove(keys: List<ReleaseKey>): Completable = releaseCache.remove(keys)

    override fun clear(): Completable = releaseCache.clear()

    private fun getSourceCombiner(releaseItems: List<Release>) =
        BiFunction<List<Episode>, List<Torrent>, List<Release>> { episodeItems, torrentItems ->
            releaseItems.map { release ->
                val episodes = episodeItems.filter { it.releaseId == release.id }
                val torrents = torrentItems.filter { it.releaseId == release.id }
                release.copy(playlist = episodes, torrents = torrents)
            }
        }

    private fun getSourceCombiner(relativeItem: Release) =
        BiFunction<List<Episode>, List<Torrent>, Release> { episodeItems, torrentItems ->
            val episodes = episodeItems.filter { it.releaseId == relativeItem.id }
            val torrents = torrentItems.filter { it.releaseId == relativeItem.id }
            relativeItem.copy(playlist = episodes, torrents = torrents)
        }

    private fun Release.toEpisodeKey() = EpisodeKey(id, null)
    private fun List<Release>.toEpisodeKeys() = map { it.toEpisodeKey() }

    private fun Release.toTorrentKey() = TorrentKey(id, null)
    private fun List<Release>.toTorrentKeys() = map { it.toTorrentKey() }
}