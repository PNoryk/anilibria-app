package anilibria.tv.api

import io.reactivex.Single
import anilibria.tv.domain.entity.menu.LinkMenu

interface MenuApiDataSource {
    fun getList(): Single<List<LinkMenu>>
}