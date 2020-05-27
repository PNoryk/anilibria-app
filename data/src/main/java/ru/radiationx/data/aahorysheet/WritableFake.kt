package ru.radiationx.data.aahorysheet

import io.reactivex.Completable

interface WritableFake<T> {

    fun putList(items: List<T>): Completable

    fun putOne(item: T): Completable

    fun clear(): Completable
}