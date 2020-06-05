package anilibria.tv.api

import io.reactivex.Single
import anilibria.tv.domain.entity.checker.Update

interface CheckerApiDataSource {
    fun get(versionCode: Int): Single<Update>
}