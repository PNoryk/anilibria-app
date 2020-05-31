package anilibria.tv.cache.impl.memory

import anilibria.tv.domain.entity.release.Torrent
import com.jakewharton.rxrelay2.BehaviorRelay
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import toothpick.InjectConstructor
import java.util.*

@InjectConstructor
class TorrentMemoryDataSource {

    private val memory = Collections.synchronizedList(mutableListOf<Torrent>())

    private val dataRelay by lazy { BehaviorRelay.createDefault(memory.toList()) }

    fun observeListAll(): Observable<List<Torrent>> = dataRelay.hide()

    fun observeList(releaseIds: List<Int>): Observable<List<Torrent>> = observeListAll()
        .map { items ->
            items.filter { releaseIds.contains(it.releaseId) }
        }

    fun getListAll(): Single<List<Torrent>> = Single.fromCallable {
        memory.toList()
    }

    fun getList(releaseIds: List<Int>): Single<List<Torrent>> = Single.fromCallable {
        memory.filter { releaseIds.contains(it.releaseId) }
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
        updateRelay()
    }

    fun removeList(ids: List<Pair<Int, Int>>): Completable = Completable.fromAction {
        synchronized(this) {
            ids.forEach { pair ->
                memory.removeAll { old ->
                    old.releaseId == pair.first && old.id == pair.second
                }
            }
        }
        updateRelay()
    }

    fun deleteAll(): Completable = Completable.fromAction {
        memory.clear()
        updateRelay()
    }

    private fun updateRelay() {
        dataRelay.accept(memory.toList())
    }
}