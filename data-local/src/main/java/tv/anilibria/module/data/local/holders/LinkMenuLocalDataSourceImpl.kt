package tv.anilibria.module.data.local.holders

import android.content.SharedPreferences
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import tv.anilibria.module.data.local.DataWrapper
import tv.anilibria.module.data.local.MoshiPreferencesPersistableData
import tv.anilibria.module.data.local.ObservableData
import tv.anilibria.module.data.local.entity.LinkMenuItemLocal
import tv.anilibria.module.data.local.mappers.toDomain
import tv.anilibria.module.data.local.mappers.toLocal
import tv.anilibria.module.domain.entity.other.LinkMenuItem

class LinkMenuLocalDataSourceImpl(
    private val preferences: SharedPreferences,
    private val moshi: Moshi
) : LinkMenuLocalDataSource {

    private val adapter by lazy {
        val type = Types.newParameterizedType(List::class.java, LinkMenuItemLocal::class.java)
        moshi.adapter<List<LinkMenuItemLocal>>(type)
    }

    private val persistableData =
        MoshiPreferencesPersistableData<List<LinkMenuItemLocal>, List<LinkMenuItem>>(
            key = "refactor.link_menu",
            adapter = adapter,
            preferences = preferences,
            read = { data -> data?.map { it.toDomain() } },
            write = { data -> data?.map { it.toLocal() } }
        )

    private val observableData = ObservableData(persistableData)

    override fun observe(): Observable<List<LinkMenuItem>> = observableData
        .observe()
        .map { it.data.orEmpty() }

    override fun get(): Single<List<LinkMenuItem>> = observableData
        .get()
        .map { it.data.orEmpty() }

    override fun put(data: List<LinkMenuItem>): Completable = observableData
        .put(DataWrapper(data))
}