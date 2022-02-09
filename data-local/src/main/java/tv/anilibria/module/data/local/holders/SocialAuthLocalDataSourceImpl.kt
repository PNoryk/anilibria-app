package tv.anilibria.module.data.local.holders

import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import tv.anilibria.module.data.local.DataWrapper
import tv.anilibria.module.data.local.ObservableData
import tv.anilibria.module.domain.entity.auth.SocialAuthService

class SocialAuthLocalDataSourceImpl : SocialAuthLocalDataSource {

    private val observableData = ObservableData<List<SocialAuthService>>()

    override fun observe(): Observable<List<SocialAuthService>> = observableData
        .observe()
        .map { it.data.orEmpty() }

    override fun get(): Single<List<SocialAuthService>> = observableData
        .get()
        .map { it.data.orEmpty() }

    override fun put(data: List<SocialAuthService>): Completable = observableData
        .put(DataWrapper(data))
}