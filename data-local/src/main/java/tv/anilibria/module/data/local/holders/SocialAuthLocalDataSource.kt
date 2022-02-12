package tv.anilibria.module.data.local.holders

import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import tv.anilibria.module.data.local.entity.SocialAuthServiceLocal
import tv.anilibria.module.data.local.mappers.toDomain
import tv.anilibria.module.data.local.mappers.toLocal
import tv.anilibria.module.domain.entity.auth.SocialAuthService
import tv.anilibria.plugin.data.storage.DataStorage
import tv.anilibria.plugin.data.storage.DataWrapper
import tv.anilibria.plugin.data.storage.MoshiStorageDataHolder
import tv.anilibria.plugin.data.storage.ObservableData

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
            key = "refactor.social_auth_services",
            adapter = adapter,
            storage = storage,
            read = { data -> data?.map { it.toDomain() } },
            write = { data -> data?.map { it.toLocal() } }
        )

    private val observableData = ObservableData(persistableData)

    fun observe(): Flow<List<SocialAuthService>> = observableData
        .observe()
        .map { it.data.orEmpty() }

    suspend fun get(): List<SocialAuthService> = observableData
        .get()
        .data.orEmpty()

    suspend fun put(data: List<SocialAuthService>) = observableData
        .put(DataWrapper(data))
}