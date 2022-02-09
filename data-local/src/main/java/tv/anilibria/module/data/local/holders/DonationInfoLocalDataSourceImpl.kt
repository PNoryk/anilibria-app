package tv.anilibria.module.data.local.holders

import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import tv.anilibria.module.data.local.DataWrapper
import tv.anilibria.module.data.local.ObservableData
import tv.anilibria.module.domain.entity.donation.DonationInfo

class DonationInfoLocalDataSourceImpl : DonationInfoLocalDataSource {

    private val observableData = ObservableData<DonationInfo>()

    override fun observe(): Observable<DataWrapper<DonationInfo>> = observableData.observe()

    override fun get(): Single<DataWrapper<DonationInfo>> = observableData.get()

    override fun put(data: DonationInfo): Completable = observableData.put(DataWrapper(data))
}