package tv.anilibria.module.domain.remote

import io.reactivex.Single
import tv.anilibria.module.domain.entity.updater.UpdateData

interface CheckerRemoteDataSource {
    fun checkUpdate(versionCode: Int): Single<UpdateData>
}