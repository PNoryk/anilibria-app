package ru.radiationx.data.entity.domain.donation.yoomoney

@Deprecated("old data")
data class YooMoneyDialog(
    val title: String,
    val help: String?,
    val amounts: Amounts?,
    val paymentTypes: PaymentTypes,
    val form: YooMoneyForm,
    val btDonateText: String,
    val btCancelText: String
) {

    companion object {
        const val TYPE_ID_ACCOUNT = "account"
        const val TYPE_ID_CARD = "card"
        const val TYPE_ID_MOBILE = "mobile"
    }

    @Deprecated("old data")
    data class Amounts(
        val title: String,
        val hint: String,
        val defaultValue: Int?,
        val items: List<Int>
    )

    @Deprecated("old data")
    data class PaymentTypes(
        val title: String,
        val selectedId: String?,
        val items: List<PaymentType>
    )

    @Deprecated("old data")
    data class PaymentType(
        val id: String,
        val title: String
    )

    @Deprecated("old data")
    data class YooMoneyForm(
        val receiver: String,
        val target: String,
        val shortDesc: String?,
        val label: String?
    )
}
