package ru.radiationx.data.api.datasource

import io.reactivex.Single
import ru.radiationx.data.adomain.entity.checker.Update

interface CheckerApiDataSource {
    fun get(): Single<Update>
}