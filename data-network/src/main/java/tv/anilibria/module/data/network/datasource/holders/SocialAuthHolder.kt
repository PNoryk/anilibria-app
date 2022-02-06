package tv.anilibria.module.data.network.datasource.holders

import io.reactivex.Observable
import tv.anilibria.module.data.network.entity.app.auth.SocialAuth

interface SocialAuthHolder {
    fun get(): List<SocialAuth>
    fun observe(): Observable<List<SocialAuth>>
    fun save(items: List<SocialAuth>)
    fun delete()
}