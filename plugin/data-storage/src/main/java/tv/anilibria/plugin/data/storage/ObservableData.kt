package tv.anilibria.plugin.data.storage

import com.jakewharton.rxrelay2.PublishRelay
import com.squareup.moshi.JsonAdapter
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicReference

interface DataStorage {
    fun getString(key: String): Single<DataWrapper<String>>
    fun setString(key: String, value: DataWrapper<String>): Completable
    fun remove(key: String): Completable
    fun clear(): Completable
}

interface DataHolder<T> {
    fun get(): Single<DataWrapper<T>>
    fun save(data: DataWrapper<T>): Completable
}

class MoshiStorageDataHolder<M, T>(
    private val key: String,
    private val adapter: JsonAdapter<M>,
    private val storage: DataStorage,
    private val read: (M?) -> T?,
    private val write: (T?) -> M?
) : DataHolder<T> {

    override fun get(): Single<DataWrapper<T>> = storage.getString(key).map { jsonString ->
        val jsonData = jsonString.data?.let { adapter.fromJson(it) }
        DataWrapper(read.invoke(jsonData))
    }

    override fun save(data: DataWrapper<T>): Completable = Completable.defer {
        val jsonString = write.invoke(data.data)?.let { jsonData -> adapter.toJson(jsonData) }
        storage.setString(key, DataWrapper(jsonString))
    }
}

class InMemoryDataHolder<T> : DataHolder<T> {

    private val atomicReference = AtomicReference<DataWrapper<T>>(DataWrapper(null))

    override fun get(): Single<DataWrapper<T>> = Single.fromCallable {
        atomicReference.get()
    }

    override fun save(data: DataWrapper<T>): Completable = Completable.fromAction {
        atomicReference.lazySet(data)
    }
}

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

data class DataWrapper<T>(val data: T?)