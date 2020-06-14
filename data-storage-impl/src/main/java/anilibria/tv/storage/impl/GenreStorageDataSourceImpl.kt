package anilibria.tv.storage.impl

import anilibria.tv.storage.GenreStorageDataSource
import anilibria.tv.storage.common.KeyValueStorage
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.reactivex.Completable
import io.reactivex.Single
import toothpick.InjectConstructor

@InjectConstructor
class GenreStorageDataSourceImpl(
    private val keyValueStorage: KeyValueStorage,
    private val gson: Gson
) : GenreStorageDataSource {

    companion object {
        private const val KEY = "local_genres"

        private val dataType = object : TypeToken<List<String>>() {}.type
    }

    override fun getList(): Single<List<String>> = keyValueStorage
        .getValue(KEY)
        .map { gson.fromJson<List<String>>(it, dataType) }
        .switchIfEmpty(Single.fromCallable { emptyList<String>() })

    override fun putList(items: List<String>): Completable = Single
        .fromCallable { gson.toJson(items, dataType) }
        .flatMapCompletable { keyValueStorage.putValue(KEY, it) }

    override fun clear(): Completable = keyValueStorage.delete(KEY)
}