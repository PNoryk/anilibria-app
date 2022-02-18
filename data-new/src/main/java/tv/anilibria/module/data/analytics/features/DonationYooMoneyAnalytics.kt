package tv.anilibria.module.data.analytics.features

import toothpick.InjectConstructor
import tv.anilibria.module.data.analytics.AnalyticsConstants
import tv.anilibria.module.data.analytics.features.extensions.toNavFromParam
import tv.anilibria.module.data.analytics.features.extensions.toParam
import tv.anilibria.module.data.analytics.features.model.AnalyticsDonationAmountType
import tv.anilibria.module.data.analytics.features.model.AnalyticsDonationPaymentType
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