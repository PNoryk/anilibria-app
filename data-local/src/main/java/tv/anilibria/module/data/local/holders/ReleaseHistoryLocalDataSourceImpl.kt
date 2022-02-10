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
import tv.anilibria.module.data.local.entity.ReleaseVisitLocal
import tv.anilibria.module.data.local.mappers.toDomain
import tv.anilibria.module.data.local.mappers.toLocal
import tv.anilibria.module.domain.entity.ReleaseVisit
import tv.anilibria.module.domain.entity.release.ReleaseId

class ReleaseHistoryLocalDataSourceImpl(
    private val preferences: SharedPreferences,
    private val moshi: Moshi
) : ReleaseHistoryLocalDataSource {

    private val adapter by lazy {
        val type = Types.newParameterizedType(List::class.java, ReleaseVisitLocal::class.java)
        moshi.adapter<List<ReleaseVisitLocal>>(type)
    }

    private val persistableData =
        MoshiPreferencesPersistableData<List<ReleaseVisitLocal>, List<ReleaseVisit>>(
            key = "refactor.release_history",
            adapter = adapter,
            preferences = preferences,
            read = { data -> data?.map { it.toDomain() } },
            write = { data -> data?.map { it.toLocal() } }
        )

    private val observableData = ObservableData(persistableData)

    override fun observe(): Observable<List<ReleaseVisit>> = observableData
        .observe()
        .map { it.data.orEmpty() }

    override fun get(): Single<List<ReleaseVisit>> = observableData
        .get()
        .map { it.data.orEmpty() }

    override fun put(data: ReleaseVisit): Completable = observableData.update { currentData ->
        val items = currentData.data.orEmpty().toMutableList()
        items.removeAll { it.id == data.id }
        items.add(data)
        DataWrapper(items)
    }

    override fun remove(releaseId: ReleaseId): Completable = observableData.update { currentData ->
        val items = currentData.data.orEmpty().filter { it.id == releaseId }
        DataWrapper(items)
    }
}