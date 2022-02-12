package tv.anilibria.plugin.data.storage

import com.squareup.moshi.JsonAdapter
import io.reactivex.Completable
import io.reactivex.Single

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