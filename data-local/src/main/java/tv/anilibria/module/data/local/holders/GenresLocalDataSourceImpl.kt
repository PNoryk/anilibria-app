package tv.anilibria.module.data.local.holders

import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import tv.anilibria.module.data.local.DataWrapper
import tv.anilibria.module.data.local.ObservableData

class GenresLocalDataSourceImpl : GenresLocalDataSource {

    private val observableData by lazy { ObservableData<List<String>>() }

    override fun observe(): Observable<List<String>> = observableData
        .observe()
        .map { it.data.orEmpty() }

    override fun get(): Single<List<String>> = observableData
        .get()
        .map { it.data.orEmpty() }

    override fun put(data: List<String>): Completable = observableData
        .put(DataWrapper(data))
}