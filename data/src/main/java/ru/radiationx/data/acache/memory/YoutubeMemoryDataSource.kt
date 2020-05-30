package ru.radiationx.data.acache.memory

import io.reactivex.Completable
import io.reactivex.Single
import ru.radiationx.data.adb.dao.YoutubeDao
import ru.radiationx.data.adb.converters.YoutubeConverter
import ru.radiationx.data.adomain.entity.youtube.Youtube
import toothpick.InjectConstructor
import java.util.*

@InjectConstructor
class YoutubeMemoryDataSource {

    private val memory = Collections.synchronizedList(mutableListOf<Youtube>())

    fun getListAll(): Single<List<Youtube>> = Single.fromCallable {
        memory.toList()
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
    }

    fun delete(): Completable = Completable.fromAction {
        memory.clear()
    }
}