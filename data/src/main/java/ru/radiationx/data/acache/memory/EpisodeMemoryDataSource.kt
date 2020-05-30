package ru.radiationx.data.acache.memory

import com.jakewharton.rxrelay2.BehaviorRelay
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import ru.radiationx.data.adb.dao.EpisodeDao
import ru.radiationx.data.adb.converters.EpisodeConverter
import ru.radiationx.data.adomain.entity.release.Episode
import toothpick.InjectConstructor
import java.lang.Exception
import java.util.*
import java.util.concurrent.CopyOnWriteArrayList

@InjectConstructor
class EpisodeMemoryDataSource() {

    private val memory = Collections.synchronizedList(mutableListOf<Episode>())

    private val dataRelay by lazy { BehaviorRelay.createDefault(memory.toList()) }

    fun observeListAll(): Observable<List<Episode>> = dataRelay.hide()

    fun observeList(releaseIds: List<Int>): Observable<List<Episode>> = observeListAll()
        .map { items ->
            items.filter { releaseIds.contains(it.releaseId) }
        }

    fun getListAll(): Single<List<Episode>> = Single.fromCallable {
        memory.toList()
    }

    fun getList(releaseIds: List<Int>): Single<List<Episode>> = Single.fromCallable {
        memory.filter { releaseIds.contains(it.releaseId) }
    }

    fun getOne(releaseId: Int, episodeId: Int): Single<Episode> = Single.fromCallable {
        memory.first { it.releaseId == releaseId && it.id == episodeId }
    }

    fun insert(items: List<Episode>): Completable = Completable.fromAction {
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