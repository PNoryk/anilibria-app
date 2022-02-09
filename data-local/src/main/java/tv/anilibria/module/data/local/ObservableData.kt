package tv.anilibria.module.data.local

import android.content.SharedPreferences
import androidx.core.content.edit
import com.jakewharton.rxrelay2.PublishRelay
import com.squareup.moshi.JsonAdapter
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicReference

interface PersistableData<T> {
    fun get(): Single<DataWrapper<T>>
    fun save(data: DataWrapper<T>): Completable
}

open class BlockingPersistableData<T>(
    private val read: () -> DataWrapper<T>,
    private val write: (DataWrapper<T>) -> Unit,
) : PersistableData<T> {

    override fun get(): Single<DataWrapper<T>> = Single.fromCallable(read)

    override fun save(data: DataWrapper<T>): Completable = Completable.fromAction {
        write.invoke(data)
    }
}

class MoshiPreferencesPersistableData<M, T>(
    private val key: String,
    private val adapter: JsonAdapter<M>,
    private val preferences: SharedPreferences,
    private val read: (M?) -> T?,
    private val write: (T?) -> M?
) : SharedPreferencesPersistableData<T>(
    preferences = preferences,
    read = {
        val jsonData = getString(key, null)?.let { adapter.fromJson(it) }
        read.invoke(jsonData)
    },
    write = {
        val jsonString = write.invoke(it)?.let { jsonData -> adapter.toJson(jsonData) }
        putString(key, jsonString)
    }
)

open class SharedPreferencesPersistableData<T>(
    private val preferences: SharedPreferences,
    private val read: SharedPreferences.() -> T?,
    private val write: SharedPreferences.Editor.(T?) -> Unit,
) : BlockingPersistableData<T>(
    read = { DataWrapper(read.invoke(preferences)) },
    write = { preferences.edit(commit = true) { write.invoke(this, it.data) } }
)

class AsyncPersistableData<T>(
    private val read: () -> Single<DataWrapper<T>>,
    private val write: (DataWrapper<T>) -> Completable,
) : PersistableData<T> {

    override fun get(): Single<DataWrapper<T>> = read.invoke()

    override fun save(data: DataWrapper<T>): Completable = write.invoke(data)
}

class InMemoryData<T> : PersistableData<T> {

    private val atomicReference = AtomicReference<DataWrapper<T>>(DataWrapper(null))

    override fun get(): Single<DataWrapper<T>> = Single.fromCallable {
        atomicReference.get()
    }

    override fun save(data: DataWrapper<T>): Completable = Completable.fromAction {
        atomicReference.lazySet(data)
    }
}

class ObservableData<T>(
    private val persistableData: PersistableData<T> = InMemoryData()
) {

    private val needUpdate = AtomicBoolean(true)

    private val triggerRelay = PublishRelay.create<Unit>()

    private val inMemoryData = InMemoryData<T>()

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