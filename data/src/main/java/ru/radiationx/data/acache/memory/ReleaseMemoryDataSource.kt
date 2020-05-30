package ru.radiationx.data.acache.memory

import io.reactivex.Completable
import io.reactivex.Single
import ru.radiationx.data.adb.dao.ReleaseDao
import ru.radiationx.data.adb.converters.ReleaseConverter
import ru.radiationx.data.adomain.entity.relative.HistoryRelative
import ru.radiationx.data.adomain.entity.release.Release
import toothpick.InjectConstructor
import java.util.*

@InjectConstructor
class ReleaseMemoryDataSource {

    private val memory = Collections.synchronizedList(mutableListOf<Release>())

    fun getListAll(): Single<List<Release>> = Single.fromCallable {
        memory.toList()
    }

    fun getOne(releaseId: Int): Single<Release> = Single.fromCallable {
        memory.first { it.id == releaseId }
    }

    fun insert(items: List<Release>): Completable = Completable.fromAction {
        synchronized(this) {
            items.forEach { new ->
                memory.removeAll { old ->
                    old.id == new.id
                }
            }
            memory.addAll(items)
        }
    }

    fun delete(): Completable = Completable.fromAction {
        memory.clear()
    }
}