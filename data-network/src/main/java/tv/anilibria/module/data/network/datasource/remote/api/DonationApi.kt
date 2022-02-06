package tv.anilibria.module.data.network.datasource.remote.api

import com.squareup.moshi.Moshi
import io.reactivex.Single
import org.json.JSONObject
import toothpick.InjectConstructor
import tv.anilibria.module.data.network.ApiClient
import tv.anilibria.module.data.network.MainClient
import tv.anilibria.module.data.network.datasource.remote.ApiResponse
import tv.anilibria.module.data.network.datasource.remote.IClient
import tv.anilibria.module.data.network.datasource.remote.address.ApiConfig
import tv.anilibria.module.data.network.entity.app.donation.DonationInfoResponse
import tv.anilibria.module.data.network.entity.app.donation.DonationInfoResponseJsonAdapter
import tv.anilibria.module.data.network.entity.domain.donation.yoomoney.YooMoneyDialog

@InjectConstructor
class DonationApi(
    @ApiClient private val client: IClient,
    @MainClient private val mainClient: IClient,
    private val apiConfig: ApiConfig,
    private val moshi: Moshi
) {

    private val dataAdapter by lazy {
        DonationInfoResponseJsonAdapter(moshi)
        moshi.adapter(DonationInfoResponse::class.java)
    }

    fun getDonationDetail(): Single<DonationInfoResponse> {
        val args: Map<String, String> = mapOf(
            "query" to "donation_details"
        )
        return client.post(apiConfig.apiUrl, args)
            .compose(ApiResponse.fetchResult<JSONObject>())
            .map { dataAdapter.fromJson(it.toString()) }
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

        return mainClient.postFull("https://yoomoney.ru/quickpay/confirm.xml", params)
            .map { it.redirect }
    }
}