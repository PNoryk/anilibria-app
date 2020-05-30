package ru.radiationx.data.acache.common

import io.reactivex.Single
import io.reactivex.SingleSource

internal fun <T> Single<List<T>>.flatMapIfListEmpty(mapper: (List<T>) -> SingleSource<List<T>>): Single<List<T>> = flatMap {
    if (it.isEmpty()) {
        mapper.invoke(it)
    } else {
        Single.just(it)
    }
}