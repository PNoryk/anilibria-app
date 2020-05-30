package ru.radiationx.data.acache.memory

import com.jakewharton.rxrelay2.BehaviorRelay
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import ru.radiationx.data.adomain.entity.relative.ScheduleDayRelative
import toothpick.InjectConstructor
import java.util.*

@InjectConstructor
class ScheduleMemoryDataSource() {

    private val memory = Collections.synchronizedList(mutableListOf<ScheduleDayRelative>())

    private val dataRelay by lazy { BehaviorRelay.createDefault(memory.toList()) }

    fun observeListAll(): Observable<List<ScheduleDayRelative>> = dataRelay.hide()

    fun getListAll(): Single<List<ScheduleDayRelative>> = Single.fromCallable {
        memory.toList()
    }

    fun getOne(dayId: Int): Single<ScheduleDayRelative> = Single.fromCallable {
        memory.first { it.dayId == dayId }
    }

    fun insert(items: List<ScheduleDayRelative>): Completable = Completable.fromAction {
        synchronized(this) {
            items.forEach { new ->
                memory.removeAll { old ->
                    old.dayId == new.dayId
                }
            }
            memory.addAll(items)
        }
        updateRelay()
    }

    fun removeList(ids: List<Int>): Completable = Completable.fromAction {
        synchronized(this) {
            ids.forEach { id ->
                memory.removeAll { old -> old.dayId == id }
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