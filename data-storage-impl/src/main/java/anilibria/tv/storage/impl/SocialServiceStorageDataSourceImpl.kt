package anilibria.tv.storage.impl

import anilibria.tv.domain.entity.auth.SocialService
import anilibria.tv.storage.SocialServiceStorageDataSource
import anilibria.tv.storage.common.KeyValueStorage
import anilibria.tv.storage.impl.converter.SocialServiceConverter
import anilibria.tv.storage.impl.entity.SocialServiceStorage
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.reactivex.Completable
import io.reactivex.Single
import toothpick.InjectConstructor

@InjectConstructor
class SocialServiceStorageDataSourceImpl(
    private val keyValueStorage: KeyValueStorage,
    private val gson: Gson,
    private val converter: SocialServiceConverter
) : SocialServiceStorageDataSource {

    companion object {
        private const val KEY = "social_auth"

        private val dataType = object : TypeToken<List<SocialServiceStorage>>() {}.type
    }

    override fun getList(): Single<List<SocialService>> = keyValueStorage
        .getValue(KEY)
        .map { gson.fromJson<List<SocialServiceStorage>>(it, dataType) }
        .map { converter.toDomain(it) }
        .switchIfEmpty(Single.just(emptyList()))

    override fun putList(items: List<SocialService>): Completable = Single
        .fromCallable { converter.toStorage(items) }
        .map { gson.toJson(it, dataType) }
        .flatMapCompletable { keyValueStorage.putValue(KEY, it) }
}