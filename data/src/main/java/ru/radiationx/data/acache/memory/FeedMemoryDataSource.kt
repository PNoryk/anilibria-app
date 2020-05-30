package ru.radiationx.data.acache.memory

import io.reactivex.Completable
import io.reactivex.Single
import ru.radiationx.data.adb.dao.FeedDao
import ru.radiationx.data.adb.converters.FeedConverter
import ru.radiationx.data.adomain.entity.relative.FeedRelative
import ru.radiationx.data.adomain.entity.release.Episode
import toothpick.InjectConstructor
import java.util.*

@InjectConstructor
class FeedMemoryDataSource {

    private val memory = Collections.synchronizedList(mutableListOf<FeedRelative>())

    fun getListAll(): Single<List<FeedRelative>> = Single.fromCallable {
        memory.toList()
    }

    fun getOne(releaseId: Int?, youtubeId: Int?): Single<FeedRelative> = Single.fromCallable {
        memory.first { it.releaseId == releaseId && it.youtubeId == youtubeId }
    }

    fun insert(items: List<FeedRelative>): Completable = Completable.fromAction {
        synchronized(this) {
            items.forEach { new ->
                memory.removeAll { old ->
                    old.releaseId == new.releaseId && old.youtubeId == new.youtubeId
                }
            }
            memory.addAll(items)
        }
    }

    fun deleteAll(): Completable = Completable.fromAction {
        memory.clear()
    }
}