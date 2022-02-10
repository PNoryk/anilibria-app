package tv.anilibria.module.data.local.holders

import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import tv.anilibria.module.data.local.DataWrapper
import tv.anilibria.module.data.local.ObservableData
import tv.anilibria.module.domain.entity.EpisodeVisit
import tv.anilibria.module.domain.entity.release.EpisodeId
import tv.anilibria.module.domain.entity.release.ReleaseId

class EpisodeHistoryLocalDataSourceImpl : EpisodeHistoryLocalDataSource {

    private val observableData = ObservableData<List<EpisodeVisit>>()

    override fun observe(): Observable<List<EpisodeVisit>> = observableData
        .observe()
        .map { it.data.orEmpty() }

    override fun get(): Single<List<EpisodeVisit>> = observableData
        .get()
        .map { it.data.orEmpty() }

    override fun put(data: EpisodeVisit): Completable = observableData.update { currentData ->
        val items = currentData.data.orEmpty().toMutableList()
        items.removeAll { it.id == data.id }
        items.add(data)
        DataWrapper(items)
    }

    override fun remove(episodeId: EpisodeId): Completable = observableData.update { currentData ->
        val items = currentData.data.orEmpty().filter { it.id == episodeId }
        DataWrapper(items)
    }

    override fun removeByRelease(releaseId: ReleaseId): Completable =
        observableData.update { currentData ->
            val items = currentData.data.orEmpty().filter { it.id.releaseId == releaseId }
            DataWrapper(items)
        }
}