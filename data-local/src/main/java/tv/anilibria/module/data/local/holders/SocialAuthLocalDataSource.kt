package tv.anilibria.module.data.local.holders

import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import tv.anilibria.module.domain.entity.auth.SocialAuthService

interface SocialAuthLocalDataSource {
    fun observe(): Observable<List<SocialAuthService>>
    fun get(): Single<List<SocialAuthService>>
    fun put(data: List<SocialAuthService>): Completable
}