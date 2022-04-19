package tv.anilibria.feature.user.data.local

import com.squareup.moshi.Moshi
import kotlinx.coroutines.flow.Flow
import toothpick.InjectConstructor
import tv.anilibria.feature.user.data.di.UserStorageQualifier
import tv.anilibria.feature.user.data.domain.User
import tv.anilibria.plugin.data.storage.DataStorage
import tv.anilibria.plugin.data.storage.MoshiStorageDataHolder
import tv.anilibria.plugin.data.storage.ObservableData
import tv.anilibria.plugin.data.storage.storageStringKey

@InjectConstructor
class UserLocalDataSource(
    @UserStorageQualifier private val storage: DataStorage,
    private val moshi: Moshi
) {

    private val adapter by lazy {
        moshi.adapter(UserLocal::class.java)
    }

    private val persistableData = MoshiStorageDataHolder<UserLocal, User>(
        key = storageStringKey("refactor.user"),
        adapter = adapter,
        storage = storage,
        read = { it?.toDomain() },
        write = { it?.toLocal() }
    )

    private val observableData = ObservableData(persistableData)

    fun observe(): Flow<User?> = observableData.observe()

    suspend fun get(): User? = observableData.get()

    suspend fun put(data: User) = observableData.put(data)

    suspend fun delete() = observableData.put(null)
}