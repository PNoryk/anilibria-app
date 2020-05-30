package ru.radiationx.data.acache.memory

import io.reactivex.Completable
import io.reactivex.Single
import ru.radiationx.data.adomain.entity.relative.ScheduleDayRelative
import toothpick.InjectConstructor
import java.util.*

@InjectConstructor
class ScheduleMemoryDataSource() {

    private val memory = Collections.synchronizedList(mutableListOf<ScheduleDayRelative>())

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
    }

    fun delete(): Completable = Completable.fromAction {
        memory.clear()
    }
}