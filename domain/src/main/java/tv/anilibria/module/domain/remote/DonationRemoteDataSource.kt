package tv.anilibria.module.domain.remote

import io.reactivex.Single
import tv.anilibria.module.domain.entity.donation.DonationInfo
import tv.anilibria.module.domain.entity.donation.yoomoney.YooMoneyDialog

interface DonationRemoteDataSource {
    fun getDonationDetail(): Single<DonationInfo>

    fun createYooMoneyPayLink(
        amount: Int,
        type: String,
        form: YooMoneyDialog.YooMoneyForm
    ): Single<String>
}