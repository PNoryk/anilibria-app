package anilibria.tv.cache.impl.memory

import anilibria.tv.domain.entity.relative.FeedRelative
import com.jakewharton.rxrelay2.BehaviorRelay
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import toothpick.InjectConstructor
import java.util.*

@InjectConstructor
class FeedMemoryDataSource {

    private val memory = Collections.synchronizedList(mutableListOf<FeedRelative>())

    private val dataRelay by lazy { BehaviorRelay.createDefault(memory.toList()) }

    fun observeListAll(): Observable<List<FeedRelative>> = dataRelay.hide()

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
        updateRelay()
    }

    fun removeList(ids: List<Pair<Int?, Int?>>): Completable = Completable.fromAction {
        synchronized(this) {
            ids.forEach { pair ->
                memory.removeAll { old ->
                    old.releaseId == pair.first && old.youtubeId == pair.second
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