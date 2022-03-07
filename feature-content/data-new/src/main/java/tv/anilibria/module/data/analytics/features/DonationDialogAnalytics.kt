package tv.anilibria.module.data.analytics.features

import toothpick.InjectConstructor
import tv.anilibria.module.data.analytics.AnalyticsConstants
import tv.anilibria.module.data.analytics.features.extensions.toLinkParam
import tv.anilibria.module.data.analytics.features.extensions.toNavFromParam
import tv.anilibria.module.data.analytics.features.extensions.toParam
import tv.anilibria.plugin.data.analytics.AnalyticsSender

@InjectConstructor
class DonationDialogAnalytics(
    private val sender: AnalyticsSender
) {

    fun open(from: String, tag: String) {
        sender.send(
            AnalyticsConstants.donation_dialog_open,
            from.toNavFromParam(),
            tag.toParam("tag")
        )
    }

    fun linkClick(fromTag: String, link: String) {
        sender.send(
            AnalyticsConstants.donation_dialog_link_click,
            link.toLinkParam(),
            fromTag.toParam("tag")
        )
    }

    fun buttonClick(fromTag: String, buttonText: String) {
        sender.send(
            AnalyticsConstants.donation_dialog_button_click,
            buttonText.toParam("text"),
            fromTag.toParam("tag")
        )
    }
}