package tv.anilibria.plugin.data.storage

import com.jakewharton.rxrelay2.PublishRelay
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import java.util.concurrent.atomic.AtomicBoolean

class ObservableData<T>(
    private val persistableData: DataHolder<T> = InMemoryDataHolder()
) {

    private val needUpdate = AtomicBoolean(true)

    private val triggerRelay = PublishRelay.create<Unit>()

    private val inMemoryData = InMemoryDataHolder<T>()

    fun observe(): Observable<DataWrapper<T>> = triggerRelay
        .startWith(Unit)
        .switchMapSingle { getActualData() }

    fun get(): Single<DataWrapper<T>> = getActualData()

    fun put(data: DataWrapper<T>): Completable = Completable.fromAction {
        persistableData.save(data)
        needUpdate.set(true)
        triggerRelay.accept(Unit)
    }

    fun update(callback: (DataWrapper<T>) -> DataWrapper<T>): Completable = get()
        .map(callback)
        .flatMapCompletable { put(it) }

    private fun getActualData(): Single<DataWrapper<T>> = Single.defer {
        if (needUpdate.compareAndSet(true, false)) {
            persistableData.get()
                .flatMapCompletable { inMemoryData.save(it) }
                .andThen(inMemoryData.get())
        } else {
            inMemoryData.get()
        }
    }
}