package ru.radiationx.data.api.service.checker

import io.reactivex.Single
import ru.radiationx.data.api.remote.checker.CheckerResponse

interface CheckerApi {

    fun get(): Single<CheckerResponse>
}