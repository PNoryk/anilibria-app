package tv.anilibria.module.data.local.holders

import kotlinx.coroutines.flow.Flow
import tv.anilibria.module.domain.entity.donation.DonationInfo
import tv.anilibria.plugin.data.storage.ObservableData

class DonationInfoLocalDataSource {

    private val observableData = ObservableData<DonationInfo>()

    fun observe(): Flow<DonationInfo?> = observableData.observe()

    suspend fun get(): DonationInfo? = observableData.get()

    suspend fun put(data: DonationInfo) = observableData.put(data)
}