package tv.anilibria.feature.donation.data.remote

import retrofit2.HttpException
import toothpick.InjectConstructor
import tv.anilibria.core.types.AbsoluteUrl
import tv.anilibria.feature.donation.data.domain.DonationInfo
import tv.anilibria.feature.donation.data.domain.yoomoney.YooMoneyDialog
import tv.anilibria.plugin.data.network.ApiWrapper
import tv.anilibria.plugin.data.network.formBodyOf
import tv.anilibria.plugin.data.restapi.handleApiResponse

@InjectConstructor
class DonationRemoteDataSource(
    private val donationApi: ApiWrapper<DonationApi>
) {

    suspend fun getDonationDetail(): DonationInfo {
        val args = formBodyOf(
            "query" to "donation_details"
        )
        return donationApi.proxy()
            .getDetails(args)
            .handleApiResponse()
            .toDomain()
    }

    // Doc https://yoomoney.ru/docs/payment-buttons/using-api/forms
    suspend fun createYooMoneyPayLink(
        amount: Int,
        type: String,
        form: YooMoneyDialog.YooMoneyForm
    ): AbsoluteUrl {
        val yooMoneyType = when (type) {
            YooMoneyDialog.TYPE_ID_ACCOUNT -> "PC"
            YooMoneyDialog.TYPE_ID_CARD -> "AC"
            YooMoneyDialog.TYPE_ID_MOBILE -> "MC"
            else -> null
        }
        val params = formBodyOf(
            "receiver" to form.receiver,
            "quickpay-form" to "shop",
            "targets" to form.target,
            "paymentType" to yooMoneyType.orEmpty(),
            "sum" to amount.toString(),
            "formcomment" to form.shortDesc.orEmpty(),
            "short-dest" to form.shortDesc.orEmpty(),
            "label" to form.label.orEmpty()
        )

        return donationApi.direct()
            .createYooMoneyPayLink("https://yoomoney.ru/quickpay/confirm.xml", params)
            .also {
                if (!it.isSuccessful) {
                    throw HttpException(it)
                }
            }
            .raw().request().url().toString()
            .let { AbsoluteUrl(it) }
    }
}