package tv.anilibria.plugin.data.storage

import io.reactivex.Completable
import io.reactivex.Single
import java.util.concurrent.atomic.AtomicReference

class InMemoryDataHolder<T> : DataHolder<T> {

    private val atomicReference = AtomicReference<DataWrapper<T>>(DataWrapper(null))

    override fun get(): Single<DataWrapper<T>> = Single.fromCallable {
        atomicReference.get()
    }

    override fun save(data: DataWrapper<T>): Completable = Completable.fromAction {
        atomicReference.lazySet(data)
    }
}