package tv.anilibria.feature.donation.data.local

import kotlinx.coroutines.flow.Flow
import toothpick.InjectConstructor
import tv.anilibria.feature.donation.data.domain.DonationInfo
import tv.anilibria.plugin.data.storage.ObservableData

@InjectConstructor
class DonationInfoLocalDataSource {

    private val observableData = ObservableData<DonationInfo>()

    fun observe(): Flow<DonationInfo?> = observableData.observe()

    suspend fun get(): DonationInfo? = observableData.get()

    suspend fun put(data: DonationInfo) = observableData.put(data)
}