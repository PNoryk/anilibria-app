package tv.anilibria.feature.appupdates.data

import com.squareup.moshi.Moshi
import io.reactivex.Single
import tv.anilibria.feature.appupdates.data.domain.UpdateData
import tv.anilibria.feature.appupdates.data.response.UpdateDataResponse
import tv.anilibria.plugin.data.network.NetworkClient
import javax.inject.Inject

class UpdatesRemoteDataSourceImpl @Inject constructor(
    private val mainClient: NetworkClient,
    private val moshi: Moshi,
    private val appDataSource: AppUpdatesRemoteDataSource,
    private val reserveSources: CheckerReserveSources
) : UpdatesRemoteDataSource {

    private val updatesAdapter by lazy {
        moshi.adapter(UpdateDataResponse::class.java)
    }

    override fun checkUpdate(versionCode: Int): Single<UpdateData> =
        appDataSource
            .checkUpdate(versionCode)
            .onErrorResumeNext { getUpdatesFromReserve() }

    private fun getUpdatesFromReserve(): Single<UpdateData> {
        val singleSources = reserveSources.sources.map { source ->
            getReserve(source)
                .map { Result.success(it) }
                .onErrorReturn { Result.failure(it) }
        }
        return Single.merge(singleSources)
            .filter { it.isSuccess }
            .map { it.getOrThrow() }
            .firstOrError()
    }

    private fun getReserve(url: String): Single<UpdateData> =
        mainClient
            .get(url, emptyMap())
            .map { updatesAdapter.fromJson(it) }
            .map { it.toDomain() }
}