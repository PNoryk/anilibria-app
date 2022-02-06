package tv.anilibria.module.data.network.entity.mapper

import tv.anilibria.module.data.network.entity.app.donation.DonationCardResponse
import tv.anilibria.module.data.network.entity.app.donation.DonationContentItemResponse
import tv.anilibria.module.data.network.entity.app.donation.DonationInfoResponse
import tv.anilibria.module.data.network.entity.app.donation.content.*
import tv.anilibria.module.data.network.entity.app.donation.content_data.DonationDialogResponse
import tv.anilibria.module.data.network.entity.domain.donation.*

fun DonationInfoResponse.toDomain() = DonationInfo(
    cardNewDonations = cards.newDonations?.toDomain(),
    cardRelease = cards.release?.toDomain(),
    detailContent = detail.content.toDomain(),
    contentDialogs = detail.contentDialogs.map { it.toDomain() },
    yooMoneyDialog = detail.yooMoneyDialog?.toDomain()
)

fun DonationCardResponse.toDomain() = DonationCard(
    title = title,
    subtitle = subtitle
)

fun List<DonationContentItemResponse>.toDomain() = mapNotNull { it.toDomain() }

fun DonationContentItemResponse.toDomain(): DonationContentItem? = when (type) {
    "button" -> requireNotNull(button).toDomain()
    "caption" -> requireNotNull(caption).toDomain()
    "divider" -> requireNotNull(divider).toDomain()
    "header" -> requireNotNull(header).toDomain()
    "section" -> requireNotNull(section).toDomain()
    else -> null
}

fun DonationContentButtonResponse.toDomain() = DonationContentButton(
    tag = tag,
    text = text,
    link = link,
    brand = brand,
    icon = icon
)

fun DonationContentCaptionResponse.toDomain() = DonationContentCaption(
    text = text
)

fun DonationContentDividerResponse.toDomain() = DonationContentDivider(
    height = height
)

fun DonationContentHeaderResponse.toDomain() = DonationContentHeader(
    text = text
)

fun DonationContentSectionResponse.toDomain() = DonationContentSection(
    title = title,
    subtitle = subtitle
)

fun DonationDialogResponse.toDomain() = DonationDialog(
    tag = tag,
    content = content.toDomain(),
    cancelText = cancelText
)
