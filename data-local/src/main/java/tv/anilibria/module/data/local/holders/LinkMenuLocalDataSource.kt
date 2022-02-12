package tv.anilibria.module.data.local.holders

import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import tv.anilibria.module.data.local.entity.LinkMenuItemLocal
import tv.anilibria.module.data.local.mappers.toDomain
import tv.anilibria.module.data.local.mappers.toLocal
import tv.anilibria.module.domain.entity.other.LinkMenuItem
import tv.anilibria.plugin.data.storage.DataStorage
import tv.anilibria.plugin.data.storage.DataWrapper
import tv.anilibria.plugin.data.storage.MoshiStorageDataHolder
import tv.anilibria.plugin.data.storage.ObservableData

class LinkMenuLocalDataSource(
    private val storage: DataStorage,
    private val moshi: Moshi
) {

    private val adapter by lazy {
        val type = Types.newParameterizedType(List::class.java, LinkMenuItemLocal::class.java)
        moshi.adapter<List<LinkMenuItemLocal>>(type)
    }

    private val persistableData =
        MoshiStorageDataHolder<List<LinkMenuItemLocal>, List<LinkMenuItem>>(
            key = "refactor.link_menu",
            adapter = adapter,
            storage = storage,
            read = { data -> data?.map { it.toDomain() } },
            write = { data -> data?.map { it.toLocal() } }
        )

    private val observableData = ObservableData(persistableData)

    fun observe(): Observable<List<LinkMenuItem>> = observableData
        .observe()
        .map { it.data.orEmpty() }

    fun get(): Single<List<LinkMenuItem>> = observableData
        .get()
        .map { it.data.orEmpty() }

    fun put(data: List<LinkMenuItem>): Completable = observableData
        .put(DataWrapper(data))
}