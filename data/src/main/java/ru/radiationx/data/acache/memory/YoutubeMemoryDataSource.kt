package ru.radiationx.data.acache.memory

import com.jakewharton.rxrelay2.BehaviorRelay
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import anilibria.tv.domain.entity.youtube.Youtube
import toothpick.InjectConstructor
import java.util.*

@InjectConstructor
class YoutubeMemoryDataSource {

    private val memory = Collections.synchronizedList(mutableListOf<Youtube>())

    private val dataRelay by lazy { BehaviorRelay.createDefault(memory.toList()) }

    fun observeListAll(): Observable<List<Youtube>> = dataRelay.hide()

    fun observeList(ids: List<Int>): Observable<List<Youtube>> = observeListAll()
        .map { youtubeList ->
            youtubeList.filter { ids.contains(it.id) }
        }

    fun getListAll(): Single<List<Youtube>> = Single.fromCallable {
        memory.toList()
    }

    fun getList(ids: List<Int>): Single<List<Youtube>> = Single.fromCallable {
        memory.filter { ids.contains(it.id) }
    }

    fun getOne(youtubeId: Int): Single<Youtube> = Single.fromCallable {
        memory.first { it.id == youtubeId }
    }

    fun insert(items: List<Youtube>): Completable = Completable.fromAction {
        synchronized(this) {
            items.forEach { new ->
                memory.removeAll { old ->
                    old.id == new.id
                }
            }
            memory.addAll(items)
        }
        updateRelay()
    }

    fun removeList(ids: List<Int>): Completable = Completable.fromAction {
        synchronized(this) {
            ids.forEach { id ->
                memory.removeAll { old -> old.id == id }
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