package ru.radiationx.data.api.datasource

import io.reactivex.Single
import anilibria.tv.domain.entity.checker.Update

interface CheckerApiDataSource {
    fun get(): Single<Update>
}