package tv.anilibria.module.data.local.holders

import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import tv.anilibria.module.data.local.DataWrapper
import tv.anilibria.module.data.local.ObservableData
import tv.anilibria.module.domain.entity.ReleaseVisit
import tv.anilibria.module.domain.entity.release.ReleaseId

class ReleaseHistoryLocalDataSourceImpl : ReleaseHistoryLocalDataSource {

    private val observableData = ObservableData<List<ReleaseVisit>>()

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