package tv.anilibria.module.data.local.holders

import android.content.SharedPreferences
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import tv.anilibria.module.data.local.DataWrapper
import tv.anilibria.module.data.local.MoshiPreferencesPersistentDataStore
import tv.anilibria.module.data.local.ObservableData
import tv.anilibria.module.data.local.entity.ReleaseVisitLocal
import tv.anilibria.module.data.local.mappers.toDomain
import tv.anilibria.module.data.local.mappers.toLocal
import tv.anilibria.module.domain.entity.ReleaseVisit
import tv.anilibria.module.domain.entity.release.ReleaseId

class ReleaseHistoryLocalDataSourceImpl(
    private val preferences: SharedPreferences,
    private val moshi: Moshi
) {

    private val adapter by lazy {
        val type = Types.newParameterizedType(List::class.java, ReleaseVisitLocal::class.java)
        moshi.adapter<List<ReleaseVisitLocal>>(type)
    }

    private val persistableData =
        MoshiPreferencesPersistentDataStore<List<ReleaseVisitLocal>, List<ReleaseVisit>>(
            key = "refactor.release_history",
            adapter = adapter,
            preferences = preferences,
            read = { data -> data?.map { it.toDomain() } },
            write = { data -> data?.map { it.toLocal() } }
        )

    private val observableData = ObservableData(persistableData)

    fun observe(): Observable<List<ReleaseVisit>> = observableData
        .observe()
        .map { it.data.orEmpty() }

    fun get(): Single<List<ReleaseVisit>> = observableData
        .get()
        .map { it.data.orEmpty() }

    fun put(data: ReleaseVisit): Completable = observableData.update { currentData ->
        val items = currentData.data?.toMutableList()?.apply {
            removeAll { it.id == data.id }
            add(data)
        }
        DataWrapper(items)
    }

    fun remove(releaseId: ReleaseId): Completable = observableData.update { currentData ->
        val items = currentData.data?.filter { it.id == releaseId }
        DataWrapper(items)
    }
}