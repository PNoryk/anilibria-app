package ru.radiationx.data.acache.combiner.impl

import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.functions.BiFunction
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

    override fun observeList(): Observable<List<Release>> = releaseCache
        .observeList()
        .switchMap { relativeItems ->
            val releaseIds = relativeItems.map { it.id }
            Observable.zip(
                episodeCache.observeList(releaseIds),
                torrentCache.observeList(releaseIds),
                getSourceCombiner(relativeItems)
            )
        }

    override fun observeList(ids: List<Int>?, codes: List<String>?): Observable<List<Release>> = releaseCache
        .observeList()
        .switchMap { relativeItems ->
            val releaseIds = relativeItems.map { it.id }
            Observable.zip(
                episodeCache.observeList(releaseIds),
                torrentCache.observeList(releaseIds),
                getSourceCombiner(relativeItems)
            )
        }

    override fun observeOne(releaseId: Int?, releaseCode: String?): Observable<Release> = releaseCache
        .observeOne(releaseId, releaseCode)
        .switchMap { relativeItem ->
            val releaseIds = listOf(relativeItem.id)
            Observable.zip(
                episodeCache.observeList(releaseIds),
                torrentCache.observeList(releaseIds),
                getSourceCombiner(relativeItem)
            )
        }

    override fun getList(): Single<List<Release>> = releaseCache
        .getList()
        .flatMap { relativeItems ->
            val releaseIds = relativeItems.map { it.id }
            Single.zip(
                episodeCache.getList(releaseIds),
                torrentCache.getList(releaseIds),
                getSourceCombiner(relativeItems)
            )
        }

    override fun getList(ids: List<Int>?, codes: List<String>?): Single<List<Release>> = releaseCache
        .getList(ids, codes)
        .flatMap { relativeItems ->
            val releaseIds = relativeItems.map { it.id }
            Single.zip(
                episodeCache.getList(releaseIds),
                torrentCache.getList(releaseIds),
                getSourceCombiner(relativeItems)
            )
        }

    override fun getOne(releaseId: Int?, releaseCode: String?): Single<Release> = releaseCache
        .getOne(releaseId, releaseCode)
        .flatMap { relativeItem ->
            val releaseIds = listOf(relativeItem.id)
            Single.zip(
                episodeCache.getList(releaseIds),
                torrentCache.getList(releaseIds),
                getSourceCombiner(relativeItem)
            )
        }

    override fun putList(items: List<Release>): Completable {
        val putEpisodes = episodeCache.putList(items.mapNotNull { it.playlist }.flatten())
        val putTorrents = torrentCache.putList(items.mapNotNull { it.torrents }.flatten())
        val putRelease = releaseCache.putList(items.map { it.copy(playlist = null, torrents = null) })
        return Completable.concat(listOf(putEpisodes, putTorrents, putRelease))
    }

    override fun removeList(items: List<Release>): Completable = releaseCache
        .removeList(items)

    override fun clear(): Completable = releaseCache.clear()

    private fun getSourceCombiner(relativeItems: List<Release>) =
        BiFunction<List<Episode>, List<Torrent>, List<Release>> { episodeItems, torrentItems ->
            relativeItems.map { release ->
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
}