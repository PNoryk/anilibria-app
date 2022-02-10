package tv.anilibria.module.data.local.holders

import android.content.SharedPreferences
import com.squareup.moshi.Moshi
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import tv.anilibria.module.data.local.entity.UserLocal
import tv.anilibria.module.data.local.mappers.toDomain
import tv.anilibria.module.data.local.mappers.toLocal
import tv.anilibria.module.domain.entity.other.User
import tv.anilibria.plugin.data.storage.DataWrapper
import tv.anilibria.plugin.data.storage.MoshiPreferencesPersistentDataStore
import tv.anilibria.plugin.data.storage.ObservableData

class UserLocalDataSourceImpl(
    private val preferences: SharedPreferences,
    private val moshi: Moshi
) {

    private val adapter by lazy {
        moshi.adapter(UserLocal::class.java)
    }

    private val persistableData = MoshiPreferencesPersistentDataStore<UserLocal, User>(
        key = "refactor.user",
        adapter = adapter,
        preferences = preferences,
        read = { it?.toDomain() },
        write = { it?.toLocal() }
    )

    private val observableData = ObservableData(persistableData)

    fun observe(): Observable<DataWrapper<User>> = observableData.observe()

    fun get(): Single<DataWrapper<User>> = observableData.get()

    fun put(data: User): Completable = observableData.put(DataWrapper(data))

    fun delete(): Completable = observableData.put(DataWrapper(null))
}