package ru.radiationx.data.analytics.features.model

@Deprecated("old data")
enum class AnalyticsDonationPaymentType(val value: String) {
    ACCOUNT("account"),
    CARD("card"),
    MOBILE("mobile")
}