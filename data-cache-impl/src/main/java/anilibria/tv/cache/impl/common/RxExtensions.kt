package anilibria.tv.cache.impl.common

import io.reactivex.Single
import io.reactivex.SingleSource

fun <T> Single<List<T>>.flatMapIfListEmpty(mapper: () -> SingleSource<List<T>>): Single<List<T>> = flatMap {
    if (it.isEmpty()) {
        mapper.invoke()
    } else {
        Single.just(it)
    }
}