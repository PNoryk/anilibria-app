package ru.radiationx.data.acache.common

import io.reactivex.Completable

interface WritableCache<T> {

    fun putList(items: List<T>): Completable

    fun removeList(items: List<T>): Completable

    fun clear(): Completable
}