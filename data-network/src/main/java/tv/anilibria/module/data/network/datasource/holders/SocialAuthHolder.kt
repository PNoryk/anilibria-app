package tv.anilibria.module.data.network.datasource.holders

import io.reactivex.Observable
import tv.anilibria.module.data.network.entity.app.auth.SocialAuthServiceResponse

interface SocialAuthHolder {
    fun get(): List<SocialAuthServiceResponse>
    fun observe(): Observable<List<SocialAuthServiceResponse>>
    fun save(items: List<SocialAuthServiceResponse>)
    fun delete()
}