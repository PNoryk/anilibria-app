package tv.anilibria.module.data.network.datasource.remote.api

import io.reactivex.Single
import tv.anilibria.module.data.network.entity.app.updater.UpdateDataResponse
import tv.anilibria.module.domain.entity.updater.UpdateData

interface CheckerRemoteDataSource {
    fun checkUpdate(versionCode: Int): Single<UpdateData>
    fun getReserve(url: String): Single<UpdateDataResponse>
}