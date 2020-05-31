package anilibria.tv.cache.impl.common

import com.jakewharton.rxrelay2.BehaviorRelay
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import java.util.*

class SimpleListMemoryCache<T> {

    private val memory = Collections.synchronizedList(mutableListOf<T>())

    private val dataRelay by lazy { BehaviorRelay.createDefault(memory.toList()) }

    fun observeList(): Observable<List<T>> = dataRelay.hide()

    fun getList(): Single<List<T>> = Single.fromCallable {
        memory.toList()
    }

    fun insert(items: List<T>): Completable = Completable.fromAction {
        synchronized(this) {
            memory.removeAll(items)
            memory.addAll(items)
        }
        updateRelay()
    }

    fun removeList(items: List<T>): Completable = Completable.fromAction {
        synchronized(this) {
            memory.removeAll(items)
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