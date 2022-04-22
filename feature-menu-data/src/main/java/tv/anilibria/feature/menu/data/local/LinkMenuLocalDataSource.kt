package tv.anilibria.feature.menu.data.local

import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import toothpick.InjectConstructor
import tv.anilibria.feature.menu.data.di.MenuStorageQualifier
import tv.anilibria.feature.menu.data.domain.LinkMenuItem
import tv.anilibria.plugin.data.storage.DataStorage
import tv.anilibria.plugin.data.storage.MoshiStorageDataHolder
import tv.anilibria.plugin.data.storage.ObservableData
import tv.anilibria.plugin.data.storage.storageStringKey

@InjectConstructor
class LinkMenuLocalDataSource(
    @MenuStorageQualifier private val storage: DataStorage,
    private val moshi: Moshi
) {

    private val adapter by lazy {
        val type = Types.newParameterizedType(List::class.java, LinkMenuItemLocal::class.java)
        moshi.adapter<List<LinkMenuItemLocal>>(type)
    }

    private val persistableData =
        MoshiStorageDataHolder<List<LinkMenuItemLocal>, List<LinkMenuItem>>(
            key = storageStringKey("refactor.link_menu"),
            adapter = adapter,
            storage = storage,
            read = { data -> data?.map { it.toDomain() } },
            write = { data -> data?.map { it.toLocal() } }
        )

    private val observableData = ObservableData(persistableData)

    fun observe(): Flow<List<LinkMenuItem>> = observableData
        .observe()
        .map { it.orEmpty() }

    suspend fun get(): List<LinkMenuItem> = observableData
        .get()
        .orEmpty()

    suspend fun put(data: List<LinkMenuItem>) = observableData.put(data)
}