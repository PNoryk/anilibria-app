package ru.radiationx.anilibria.model.data.holders

import io.reactivex.Observable

/**
 * Created by radiationx on 30.12.17.
 */
interface AuthHolder {
    fun observeVkAuthChange(): Observable<Boolean>
    fun changeVkAuth(value: Boolean)
}