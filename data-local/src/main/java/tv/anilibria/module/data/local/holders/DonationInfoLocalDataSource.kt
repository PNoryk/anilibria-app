package tv.anilibria.module.data.local.holders

import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import tv.anilibria.module.domain.entity.donation.DonationInfo

interface DonationInfoLocalDataSource {
    fun observe(): Observable<DonationInfo>
    fun get(): Single<DonationInfo>
    fun put(data: DonationInfo): Completable
}