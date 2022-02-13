package tv.anilibria.module.data.repos

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import toothpick.InjectConstructor
import tv.anilibria.module.data.local.holders.DonationInfoLocalDataSource
import tv.anilibria.module.data.restapi.datasource.remote.api.DonationRemoteDataSource
import tv.anilibria.module.domain.entity.donation.DonationInfo
import tv.anilibria.module.domain.entity.donation.yoomoney.YooMoneyDialog

@InjectConstructor
class DonationRepository(
    private val donationApi: DonationRemoteDataSource,
    private val donationHolder: DonationInfoLocalDataSource,
) {
    fun observerDonationInfo(): Flow<DonationInfo> {
        return donationHolder.observe().filterNotNull()
    }

    suspend fun requestUpdate() {
        donationApi.getDonationDetail().also {
            donationHolder.put(it)
        }
    }

    suspend fun createYooMoneyPayLink(
        amount: Int,
        type: String,
        form: YooMoneyDialog.YooMoneyForm
    ): String {
        return donationApi.createYooMoneyPayLink(amount, type, form)
    }
}