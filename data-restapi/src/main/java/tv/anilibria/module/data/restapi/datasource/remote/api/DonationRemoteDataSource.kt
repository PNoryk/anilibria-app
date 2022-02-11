package tv.anilibria.module.data.restapi.datasource.remote.api

import com.squareup.moshi.Moshi
import io.reactivex.Single
import toothpick.InjectConstructor
import tv.anilibria.module.data.restapi.datasource.remote.retrofit.DonationApi
import tv.anilibria.module.data.restapi.entity.app.donation.DonationInfoResponse
import tv.anilibria.module.data.restapi.entity.mapper.donation.toDomain
import tv.anilibria.module.domain.entity.donation.DonationInfo
import tv.anilibria.module.domain.entity.donation.yoomoney.YooMoneyDialog
import tv.anilibria.plugin.data.network.formBodyOf
import tv.anilibria.plugin.data.restapi.*

@InjectConstructor
class DonationRemoteDataSource(
    private val apiClient: ApiNetworkClient,
    private val mainClient: DefaultNetworkClient,
    private val donationApi: ApiWrapper<DonationApi>,
    private val apiConfig: ApiConfigProvider,
    private val moshi: Moshi
) {

    fun getDonationDetail(): Single<DonationInfo> {
        val args = formBodyOf(
            "query" to "donation_details"
        )
        return donationApi.proxy()
            .getDetails(args)
            .handleApiResponse()
            .map { it.toDomain() }
    }

    // Doc https://yoomoney.ru/docs/payment-buttons/using-api/forms
    fun createYooMoneyPayLink(
        amount: Int,
        type: String,
        form: YooMoneyDialog.YooMoneyForm
    ): Single<String> {
        val yooMoneyType = when (type) {
            YooMoneyDialog.TYPE_ID_ACCOUNT -> "PC"
            YooMoneyDialog.TYPE_ID_CARD -> "AC"
            YooMoneyDialog.TYPE_ID_MOBILE -> "MC"
            else -> null
        }
        val params = mapOf(
            "receiver" to form.receiver,
            "quickpay-form" to "shop",
            "targets" to form.target,
            "paymentType" to yooMoneyType.orEmpty(),
            "sum" to amount.toString(),
            "formcomment" to form.shortDesc.orEmpty(),
            "short-dest" to form.shortDesc.orEmpty(),
            "label" to form.label.orEmpty()
        )

        return mainClient.post("https://yoomoney.ru/quickpay/confirm.xml", params)
            .map { it.redirect }
    }
}