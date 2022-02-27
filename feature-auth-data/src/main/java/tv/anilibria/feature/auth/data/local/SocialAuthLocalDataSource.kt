package tv.anilibria.feature.auth.data.local

import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import tv.anilibria.feature.auth.data.domain.SocialAuthService
import tv.anilibria.plugin.data.storage.DataStorage
import tv.anilibria.plugin.data.storage.MoshiStorageDataHolder
import tv.anilibria.plugin.data.storage.ObservableData
import tv.anilibria.plugin.data.storage.storageStringKey

class SocialAuthLocalDataSource(
    private val storage: DataStorage,
    private val moshi: Moshi
) {

    private val adapter by lazy {
        val type = Types.newParameterizedType(List::class.java, SocialAuthServiceLocal::class.java)
        moshi.adapter<List<SocialAuthServiceLocal>>(type)
    }

    private val persistableData =
        MoshiStorageDataHolder<List<SocialAuthServiceLocal>, List<SocialAuthService>>(
            key = storageStringKey("refactor.social_auth_services"),
            adapter = adapter,
            storage = storage,
            read = { data -> data?.map { it.toDomain() } },
            write = { data -> data?.map { it.toLocal() } }
        )

    private val observableData = ObservableData(persistableData)

    fun observe(): Flow<List<SocialAuthService>> = observableData
        .observe()
        .map { it.orEmpty() }

    suspend fun get(): List<SocialAuthService> = observableData
        .get()
        .orEmpty()

    suspend fun put(data: List<SocialAuthService>) = observableData
        .put(data)
}