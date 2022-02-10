package tv.anilibria.module.data.local.holders

import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import tv.anilibria.plugin.data.storage.DataWrapper
import tv.anilibria.plugin.data.storage.ObservableData
import tv.anilibria.module.domain.entity.donation.DonationInfo

class DonationInfoLocalDataSource {

    private val observableData = ObservableData<DonationInfo>()

    fun observe(): Observable<DataWrapper<DonationInfo>> = observableData.observe()

    fun get(): Single<DataWrapper<DonationInfo>> = observableData.get()

    fun put(data: DonationInfo): Completable = observableData.put(DataWrapper(data))
}