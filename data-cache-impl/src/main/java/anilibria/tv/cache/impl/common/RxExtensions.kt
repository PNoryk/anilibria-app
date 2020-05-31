package anilibria.tv.cache.impl.common

import io.reactivex.Single
import io.reactivex.SingleSource

fun <T> Single<List<T>>.flatMapIfListEmpty(mapper: (List<T>) -> SingleSource<List<T>>): Single<List<T>> = flatMap {
    if (it.isEmpty()) {
        mapper.invoke(it)
    } else {
        Single.just(it)
    }
}