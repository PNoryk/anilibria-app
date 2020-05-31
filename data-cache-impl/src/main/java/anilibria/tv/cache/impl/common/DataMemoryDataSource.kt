package anilibria.tv.cache.impl.common

import com.jakewharton.rxrelay2.BehaviorRelay
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.Single
import java.util.concurrent.atomic.AtomicReference

open class DataMemoryDataSource<T> {

    private var currentData: AtomicReference<T> = AtomicReference()

    private val dataRelay by lazy { BehaviorRelay.create<T>() }

    fun observeData(): Observable<T> = dataRelay.hide().filter { currentData.get() != null }

    fun getData(): Maybe<T> = Maybe.fromCallable {
        currentData.get()
    }

    fun insert(data: T): Completable = Completable.fromAction {
        currentData.set(data)
        updateRelay()
    }

    fun delete(): Completable = Completable.fromAction {
        currentData.set(null)
        updateRelay()
    }

    private fun updateRelay() {
        currentData.get()?.also { dataRelay.accept(it) }
    }
}