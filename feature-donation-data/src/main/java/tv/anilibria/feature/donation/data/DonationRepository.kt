package tv.anilibria.feature.donation.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import toothpick.InjectConstructor
import tv.anilibria.core.types.AbsoluteUrl
import tv.anilibria.feature.donation.data.domain.DonationInfo
import tv.anilibria.feature.donation.data.domain.yoomoney.YooMoneyDialog
import tv.anilibria.feature.donation.data.local.DonationInfoLocalDataSource
import tv.anilibria.feature.donation.data.remote.DonationRemoteDataSource

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
    ): AbsoluteUrl {
        return donationApi.createYooMoneyPayLink(amount, type, form)
    }
}