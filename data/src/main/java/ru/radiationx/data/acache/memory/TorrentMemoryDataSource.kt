package ru.radiationx.data.acache.memory

import io.reactivex.Completable
import io.reactivex.Single
import ru.radiationx.data.adb.dao.TorrentDao
import ru.radiationx.data.adb.converters.TorrentConverter
import ru.radiationx.data.adomain.entity.release.Episode
import ru.radiationx.data.adomain.entity.release.Torrent
import toothpick.InjectConstructor
import java.util.*

@InjectConstructor
class TorrentMemoryDataSource {

    private val memory = Collections.synchronizedList(mutableListOf<Torrent>())

    fun getListAll(): Single<List<Torrent>> = Single.fromCallable {
        memory.toList()
    }

    fun getList(releaseId: Int): Single<List<Torrent>> = Single.fromCallable {
        memory.filter { it.releaseId == releaseId }
    }

    fun getOne(releaseId: Int, episodeId: Int): Single<Torrent> = Single.fromCallable {
        memory.first { it.releaseId == releaseId && it.id == episodeId }
    }

    fun insert(items: List<Torrent>): Completable = Completable.fromAction {
        synchronized(this) {
            items.forEach { new ->
                memory.removeAll { old ->
                    old.releaseId == new.releaseId && old.id == new.id
                }
            }
            memory.addAll(items)
        }
    }

    fun delete(): Completable = Completable.fromAction {
        memory.clear()
    }
}