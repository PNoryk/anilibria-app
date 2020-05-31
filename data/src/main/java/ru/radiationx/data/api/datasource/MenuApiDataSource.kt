package ru.radiationx.data.api.datasource

import io.reactivex.Single
import ru.radiationx.data.adomain.entity.menu.LinkMenu

interface MenuApiDataSource {
    fun getList(): Single<List<LinkMenu>>
}