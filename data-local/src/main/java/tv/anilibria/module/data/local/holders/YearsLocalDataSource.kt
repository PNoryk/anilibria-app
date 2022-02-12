package tv.anilibria.module.data.local.holders

import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import tv.anilibria.plugin.data.storage.DataStorage
import tv.anilibria.plugin.data.storage.DataWrapper
import tv.anilibria.plugin.data.storage.MoshiStorageDataHolder
import tv.anilibria.plugin.data.storage.ObservableData

class YearsLocalDataSource(
    private val storage: DataStorage,
    private val moshi: Moshi
) {

    private val adapter by lazy {
        val type = Types.newParameterizedType(List::class.java, String::class.java)
        moshi.adapter<List<String>>(type)
    }

    private val persistableData = MoshiStorageDataHolder<List<String>, List<String>>(
        key = "refactor.years",
        adapter = adapter,
        storage = storage,
        read = { it },
        write = { it }
    )

    private val observableData = ObservableData(persistableData)

    fun observe(): Observable<List<String>> = observableData
        .observe()
        .map { it.data.orEmpty() }

    fun get(): Single<List<String>> = observableData
        .get()
        .map { it.data.orEmpty() }

    fun put(data: List<String>): Completable = observableData
        .put(DataWrapper(data))
}