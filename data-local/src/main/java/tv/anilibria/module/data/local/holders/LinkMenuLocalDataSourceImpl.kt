package tv.anilibria.module.data.local.holders

import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import tv.anilibria.module.data.local.DataWrapper
import tv.anilibria.module.data.local.ObservableData
import tv.anilibria.module.domain.entity.other.LinkMenuItem

class LinkMenuLocalDataSourceImpl : LinkMenuLocalDataSource {

    private val observableData = ObservableData<List<LinkMenuItem>>()

    override fun observe(): Observable<List<LinkMenuItem>> = observableData
        .observe()
        .map { it.data.orEmpty() }

    override fun get(): Single<List<LinkMenuItem>> = observableData
        .get()
        .map { it.data.orEmpty() }

    override fun put(data: List<LinkMenuItem>): Completable = observableData
        .put(DataWrapper(data))
}