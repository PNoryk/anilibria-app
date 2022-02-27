package tv.anilibria.feature.donation.data.remote

import tv.anilibria.feature.donation.data.remote.entity.content_data.YooMoneyDialogResponse
import tv.anilibria.feature.donation.data.domain.yoomoney.YooMoneyDialog

fun YooMoneyDialogResponse.toDomain() = YooMoneyDialog(
    title = title,
    help = help,
    amounts = amounts?.toDomain(),
    paymentTypes = paymentTypes.toDomain(),
    form = form.toDomain(),
    btDonateText = btDonateText,
    btCancelText = btCancelText
)

fun YooMoneyDialogResponse.Amounts.toDomain() = YooMoneyDialog.Amounts(
    title = title,
    hint = hint,
    defaultValue = defaultValue,
    items = items
)

fun YooMoneyDialogResponse.PaymentTypes.toDomain() = YooMoneyDialog.PaymentTypes(
    title = title,
    selectedId = selectedId,
    items = items.map { it.toDomain() }
)

fun YooMoneyDialogResponse.PaymentType.toDomain() = YooMoneyDialog.PaymentType(
    id = id,
    title = title
)

fun YooMoneyDialogResponse.YooMoneyForm.toDomain() = YooMoneyDialog.YooMoneyForm(
    receiver = receiver,
    target = target,
    shortDesc = shortDesc,
    label = label
)