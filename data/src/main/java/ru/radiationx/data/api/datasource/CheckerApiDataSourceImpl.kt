package ru.radiationx.data.api.datasource

import io.reactivex.Single
import ru.radiationx.data.SharedBuildConfig
import ru.radiationx.data.adomain.entity.checker.Update
import ru.radiationx.data.api.common.handleApiResponse
import ru.radiationx.data.api.converter.CheckerConverter
import ru.radiationx.data.api.service.CheckerService
import toothpick.InjectConstructor

@InjectConstructor
class CheckerApiDataSourceImpl(
    private val checkerService: CheckerService,
    private val buildConfig: SharedBuildConfig,
    private val checkerConverter: CheckerConverter
) {

    fun get(): Single<Update> = checkerService
        .get(
            mapOf(
                "query" to "app_update",
                "current" to buildConfig.versionCode.toString()
            )
        )
        .handleApiResponse()
        .map { checkerConverter.toDomain(it.update) }
}