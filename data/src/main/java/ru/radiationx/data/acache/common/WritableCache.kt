package ru.radiationx.data.acache.common

import io.reactivex.Completable

interface WritableCache<T> {

    fun putList(items: List<T>): Completable

    fun putOne(item: T): Completable

    fun removeOne(item: T): Completable

    fun clear(): Completable
}