package ru.radiationx.data.acache.common

import io.reactivex.Observable
import io.reactivex.Single

interface ReadableCache<T> {

    fun observeList(): Observable<List<T>>

    fun fetchList(): Single<List<T>>

    fun fetchOne(id: Int): Single<T>

}