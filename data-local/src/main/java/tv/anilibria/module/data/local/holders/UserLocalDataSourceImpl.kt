package tv.anilibria.module.data.local.holders

import android.content.SharedPreferences
import com.squareup.moshi.Moshi
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import tv.anilibria.module.data.local.DataWrapper
import tv.anilibria.module.data.local.MoshiPreferencesPersistableData
import tv.anilibria.module.data.local.ObservableData
import tv.anilibria.module.data.local.entity.UserLocal
import tv.anilibria.module.data.local.mappers.toDomain
import tv.anilibria.module.data.local.mappers.toLocal
import tv.anilibria.module.domain.entity.other.User

class UserLocalDataSourceImpl(
    private val preferences: SharedPreferences,
    private val moshi: Moshi
) : UserLocalDataSource {

    private val adapter by lazy {
        moshi.adapter(UserLocal::class.java)
    }

    private val persistableData = MoshiPreferencesPersistableData<UserLocal, User>(
        key = "refactor.user",
        adapter = adapter,
        preferences = preferences,
        read = { it?.toDomain() },
        write = { it?.toLocal() }
    )

    private val observableData = ObservableData(persistableData)

    override fun observe(): Observable<DataWrapper<User>> = observableData.observe()

    override fun get(): Single<DataWrapper<User>> = observableData.get()

    override fun put(data: User): Completable = observableData.put(DataWrapper(data))

    override fun delete(): Completable = observableData.put(DataWrapper(null))
}