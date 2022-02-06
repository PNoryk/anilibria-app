package tv.anilibria.module.data.network.datasource.holders

import io.reactivex.Observable

/**
 * Created by radiationx on 30.12.17.
 */
interface AuthHolder {
    fun observeVkAuthChange(): Observable<Boolean>
    fun changeVkAuth(value: Boolean)
    fun getDeviceId(): String
}