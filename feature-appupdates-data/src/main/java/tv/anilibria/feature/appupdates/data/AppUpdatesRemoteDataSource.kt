package tv.anilibria.feature.appupdates.data

import io.reactivex.Single
import tv.anilibria.feature.appupdates.data.domain.UpdateData

interface AppUpdatesRemoteDataSource {
    fun checkUpdate(versionCode: Int): Single<UpdateData>
}