package anilibria.tv.db.impl.common

import io.reactivex.Single


fun <T> zipCollections(sources: Collection<Single<List<T>>>): Single<List<T>> = Single.zip(sources) {
    it as Array<List<T>>
    it.toList().flatten()
}