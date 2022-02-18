package tv.anilibria.module.data.analytics.features

import toothpick.InjectConstructor
import tv.anilibria.module.data.analytics.AnalyticsConstants
import tv.anilibria.module.data.analytics.features.extensions.toLinkParam
import tv.anilibria.module.data.analytics.features.extensions.toNavFromParam
import tv.anilibria.module.data.analytics.features.extensions.toParam
import tv.anilibria.plugin.data.analytics.AnalyticsSender

@InjectConstructor
class DonationDetailAnalytics(
    private val sender: AnalyticsSender
) {

    fun open(from: String) {
        sender.send(
            AnalyticsConstants.donation_detail_open,
            from.toNavFromParam()
        )
    }

    fun linkClick(link: String) {
        sender.send(
            AnalyticsConstants.donation_detail_link_click,
            link.toLinkParam()
        )
    }

    fun buttonClick(buttonText: String) {
        sender.send(
            AnalyticsConstants.donation_detail_button_click,
            buttonText.toParam("text")
        )
    }
}