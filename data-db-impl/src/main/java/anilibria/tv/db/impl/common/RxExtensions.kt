package anilibria.tv.db.impl.common

import io.reactivex.Single


fun <T> zipCollections(sources: Collection<Single<List<T>>>): Single<List<T>> = Single.zip(sources) { arrayOfAnys ->
    arrayOfAnys.map { it as List<T> }.flatten()
}