package anilibria.tv.storage

import anilibria.tv.domain.entity.menu.LinkMenu
import io.reactivex.Completable
import io.reactivex.Single

interface LinkMenuStorageDataSource {

    fun getList(): Single<List<LinkMenu>>

    fun putList(items: List<LinkMenu>): Completable
}