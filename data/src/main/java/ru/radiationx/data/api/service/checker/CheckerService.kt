package ru.radiationx.data.api.service.checker

import io.reactivex.Single
import ru.radiationx.data.SharedBuildConfig
import ru.radiationx.data.adomain.checker.Update
import ru.radiationx.data.api.remote.checker.CheckerResponse
import ru.radiationx.data.api.common.handleApiResponse
import ru.radiationx.data.api.converter.CheckerConverter

class CheckerService(
    private val checkerApi: CheckerApi,
    private val buildConfig: SharedBuildConfig,
    private val checkerConverter: CheckerConverter
) {

    fun get(): Single<Update> = checkerApi
        .get(
            mapOf(
                "query" to "app_update",
                "current" to buildConfig.versionCode.toString()
            )
        )
        .handleApiResponse()
        .map { checkerConverter.toDomain(it.update) }
}