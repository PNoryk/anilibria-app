package ru.radiationx.data.acache.memory

import io.reactivex.Completable
import io.reactivex.Single
import ru.radiationx.data.adb.dao.HistoryDao
import ru.radiationx.data.adb.converters.HistoryConverter
import ru.radiationx.data.adomain.entity.relative.FeedRelative
import ru.radiationx.data.adomain.entity.relative.HistoryRelative
import toothpick.InjectConstructor
import java.util.*

@InjectConstructor
class HistoryMemoryDataSource {

    private val memory = Collections.synchronizedList(mutableListOf<HistoryRelative>())

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
    }

    fun deleteAll(): Completable = Completable.fromAction {
        memory.clear()
    }
}