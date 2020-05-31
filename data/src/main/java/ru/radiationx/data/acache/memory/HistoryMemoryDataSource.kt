package ru.radiationx.data.acache.memory

import com.jakewharton.rxrelay2.BehaviorRelay
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import anilibria.tv.domain.entity.relative.HistoryRelative
import toothpick.InjectConstructor
import java.util.*

@InjectConstructor
class HistoryMemoryDataSource {

    private val memory = Collections.synchronizedList(mutableListOf<HistoryRelative>())

    private val dataRelay by lazy { BehaviorRelay.createDefault(memory.toList()) }

    fun observeListAll(): Observable<List<HistoryRelative>> = dataRelay.hide()

    fun getListAll(): Single<List<HistoryRelative>> = Single.fromCallable {
        memory.toList()
    }

    fun getOne(releaseId: Int): Single<HistoryRelative> = Single.fromCallable {
        memory.first { it.releaseId == releaseId }
    }

    fun insert(items: List<HistoryRelative>): Completable = Completable.fromAction {
        synchronized(this) {
            items.forEach { new ->
                memory.removeAll { old ->
                    old.releaseId == new.releaseId
                }
            }
            memory.addAll(items)
        }
        updateRelay()
    }

    fun removeList(ids: List<Int>): Completable = Completable.fromAction {
        synchronized(this) {
            ids.forEach { id ->
                memory.removeAll { old ->
                    old.releaseId == id
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