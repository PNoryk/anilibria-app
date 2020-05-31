package anilibria.tv.storage

import anilibria.tv.domain.entity.auth.SocialService
import anilibria.tv.domain.entity.menu.LinkMenu
import io.reactivex.Completable
import io.reactivex.Single

interface SocialServiceStorageDataSource {

    fun getList(): Single<List<SocialService>>

    fun putList(items: List<SocialService>): Completable
}