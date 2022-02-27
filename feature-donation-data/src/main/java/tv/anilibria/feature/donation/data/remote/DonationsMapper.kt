package tv.anilibria.feature.donation.data.remote

import tv.anilibria.core.types.asAbsoluteUrl
import tv.anilibria.core.types.asHtmlText
import tv.anilibria.feature.donation.data.domain.*
import tv.anilibria.feature.donation.data.remote.entity.DonationCardResponse
import tv.anilibria.feature.donation.data.remote.entity.DonationContentItemResponse
import tv.anilibria.feature.donation.data.remote.entity.DonationInfoResponse
import tv.anilibria.feature.donation.data.remote.entity.content.*
import tv.anilibria.feature.donation.data.remote.entity.content_data.DonationDialogResponse
import tv.anilibria.module.data.restapi.entity.mapper.toDataColor
import tv.anilibria.module.data.restapi.entity.mapper.toDataIcon

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
    link = link?.asAbsoluteUrl(),
    color = brand?.toDataColor(),
    icon = icon?.toDataIcon()
)

fun DonationContentCaptionResponse.toDomain() = DonationContentCaption(
    text = text.asHtmlText()
)

fun DonationContentDividerResponse.toDomain() = DonationContentDivider(
    height = height
)

fun DonationContentHeaderResponse.toDomain() = DonationContentHeader(
    text = text
)

fun DonationContentSectionResponse.toDomain() = DonationContentSection(
    title = title,
    subtitle = subtitle?.asHtmlText()
)

fun DonationDialogResponse.toDomain() = DonationDialog(
    tag = tag,
    content = content.toDomain(),
    cancelText = cancelText
)
