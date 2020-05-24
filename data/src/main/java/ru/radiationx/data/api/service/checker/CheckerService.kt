package ru.radiationx.data.api.service.checker

import io.reactivex.Single
import ru.radiationx.data.SharedBuildConfig
import ru.radiationx.data.api.remote.checker.CheckerResponse
import ru.radiationx.data.api.common.handleApiResponse
import ru.radiationx.data.api.remote.request.putNotNull
import ru.radiationx.data.api.remote.request.startQuery

class CheckerService(
    private val checkerApi: CheckerApi,
    private val buildConfig: SharedBuildConfig
) {

    fun get(): Single<CheckerResponse> = checkerApi
        .get(
            mapOf(
                "query" to "app_update",
                "current" to buildConfig.versionCode.toString()
            )
        )
        .handleApiResponse()
}