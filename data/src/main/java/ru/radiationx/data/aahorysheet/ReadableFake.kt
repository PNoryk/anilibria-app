package ru.radiationx.data.aahorysheet

import io.reactivex.Observable
import io.reactivex.Single

interface ReadableFake<T> {

    fun observeChanges(): Observable<List<T>>

    fun getList(): Single<List<T>>

    fun getOne(id: Int): Single<T>

}