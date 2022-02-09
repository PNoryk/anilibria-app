package tv.anilibria.module.data.local.holders

import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import tv.anilibria.module.data.local.DataWrapper
import tv.anilibria.module.domain.entity.donation.DonationInfo

interface DonationInfoLocalDataSource {
    fun observe(): Observable<DataWrapper<DonationInfo>>
    fun get(): Single<DataWrapper<DonationInfo>>
    fun put(data: DonationInfo): Completable
}