package ru.radiationx.data.aahorysheet

import io.reactivex.Completable
import io.reactivex.Single
import ru.radiationx.data.adomain.youtube.YouTube

interface WritableFake<T> {

    fun putList(items: List<T>): Completable

    fun putOne(item: T): Completable

    fun clear(): Completable
}