package tv.anilibria.feature.analytics.api.features

import toothpick.InjectConstructor
import tv.anilibria.feature.analytics.api.AnalyticsConstants
import tv.anilibria.feature.analytics.api.features.extensions.toNavFromParam
import tv.anilibria.feature.analytics.api.features.extensions.toParam
import tv.anilibria.feature.analytics.api.features.model.AnalyticsDonationAmountType
import tv.anilibria.feature.analytics.api.features.model.AnalyticsDonationPaymentType
import tv.anilibria.plugin.data.analytics.AnalyticsSender

@InjectConstructor
class DonationYooMoneyAnalytics(
    private val sender: AnalyticsSender
) {

    private companion object {
        const val PARAM_AMOUNT = "amount"
        const val PARAM_AMOUNT_TYPE = "amount_type"
        const val PARAM_PAYMENT_TYPE = "payment_type"
    }

    fun open(from: String) {
        sender.send(
            AnalyticsConstants.donation_yoomoney_open,
            from.toNavFromParam()
        )
    }

    fun helpClick() {
        sender.send(AnalyticsConstants.donation_yoomoney_help_click)
    }

    fun acceptClick(
        amount: Int?,
        amountType: AnalyticsDonationAmountType?,
        paymentType: AnalyticsDonationPaymentType?
    ) {
        sender.send(
            AnalyticsConstants.donation_yoomoney_accept_click,
            amount.toParam(PARAM_AMOUNT),
            amountType.toParam(PARAM_AMOUNT_TYPE),
            paymentType.toParam(PARAM_PAYMENT_TYPE)
        )
    }


}