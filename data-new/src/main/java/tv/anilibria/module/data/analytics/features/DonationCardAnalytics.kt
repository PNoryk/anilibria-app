package tv.anilibria.module.data.analytics.features

import toothpick.InjectConstructor
import tv.anilibria.module.data.analytics.AnalyticsConstants
import tv.anilibria.module.data.analytics.features.extensions.toNavFromParam
import tv.anilibria.plugin.data.analytics.AnalyticsSender

@InjectConstructor
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