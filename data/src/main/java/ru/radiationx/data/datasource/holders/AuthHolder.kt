package ru.radiationx.data.datasource.holders

import io.reactivex.Observable

/**
 * Created by radiationx on 30.12.17.
 */
@Deprecated("old data")
interface AuthHolder {
    fun observeVkAuthChange(): Observable<Boolean>
    fun changeVkAuth(value: Boolean)
}