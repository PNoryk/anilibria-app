package anilibria.tv.api.impl.datasource

import io.reactivex.Single
import ru.radiationx.data.SharedBuildConfig
import anilibria.tv.domain.entity.checker.Update
import anilibria.tv.api.impl.common.handleApiResponse
import anilibria.tv.api.impl.converter.CheckerConverter
import anilibria.tv.api.CheckerApiDataSource
import anilibria.tv.api.impl.service.CheckerService
import toothpick.InjectConstructor

@InjectConstructor
class CheckerApiDataSourceImpl(
    private val checkerService: CheckerService,
    private val buildConfig: SharedBuildConfig,
    private val checkerConverter: CheckerConverter
) : CheckerApiDataSource {

    override fun get(): Single<Update> = checkerService
        .get(
            mapOf(
                "query" to "app_update",
                "current" to buildConfig.versionCode.toString()
            )
        )
        .handleApiResponse()
        .map { checkerConverter.toDomain(it.update) }
}