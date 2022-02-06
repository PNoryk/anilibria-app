package tv.anilibria.module.data.network.datasource.holders

import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import tv.anilibria.module.data.network.entity.app.donation.DonationDetailResponse
import tv.anilibria.module.data.network.entity.app.donation.DonationInfoResponse
import tv.anilibria.module.data.network.entity.domain.donation.DonationInfo

interface DonationHolder {
    fun observe(): Observable<DonationInfoResponse>
    fun get(): Single<DonationInfoResponse>
    fun save(data: DonationInfoResponse): Completable
    fun delete(): Completable
}