package tv.anilibria.module.data.restapi.entity.mapper.donation

import tv.anilibria.module.data.restapi.entity.app.donation.DonationCardResponse
import tv.anilibria.module.data.restapi.entity.app.donation.DonationContentItemResponse
import tv.anilibria.module.data.restapi.entity.app.donation.DonationInfoResponse
import tv.anilibria.module.data.restapi.entity.app.donation.content.*
import tv.anilibria.module.data.restapi.entity.app.donation.content_data.DonationDialogResponse
import tv.anilibria.module.data.restapi.entity.mapper.toDataColor
import tv.anilibria.module.data.restapi.entity.mapper.toDataIcon
import tv.anilibria.module.domain.entity.common.asAbsoluteUrl
import tv.anilibria.module.domain.entity.common.asHtmlText
import tv.anilibria.module.domain.entity.donation.*

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
