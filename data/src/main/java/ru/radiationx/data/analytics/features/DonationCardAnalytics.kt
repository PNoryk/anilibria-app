package ru.radiationx.data.analytics.features

import ru.radiationx.data.analytics.AnalyticsConstants
import ru.radiationx.data.analytics.AnalyticsSender
import ru.radiationx.data.analytics.features.extensions.toNavFromParam
import toothpick.InjectConstructor

@InjectConstructor
@Deprecated("old data")
class DonationCardAnalytics(
    private val sender: AnalyticsSender
) {

    fun onNewDonationClick(from: String) {
        sender.send(
            AnalyticsConstants.donation_card_new_click,
            from.toNavFromParam()
        )
    }

    fun onNewDonationCloseClick(from: String) {
        sender.send(
            AnalyticsConstants.donation_card_new_close_click,
            from.toNavFromParam()
        )
    }
}