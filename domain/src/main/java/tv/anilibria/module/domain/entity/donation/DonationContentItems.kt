package tv.anilibria.module.domain.entity.donation

import tv.anilibria.module.domain.entity.common.AbsoluteUrl
import tv.anilibria.module.domain.entity.common.HtmlText

sealed class DonationContentItem

data class DonationContentButton(
    val tag: String?,
    val text: String,
    val link: AbsoluteUrl?,
    val brand: String?,
    val icon: String?
) : DonationContentItem()

data class DonationContentCaption(
    val text: HtmlText
) : DonationContentItem()

data class DonationContentDivider(
    val height: Int
) : DonationContentItem()

data class DonationContentHeader(
    val text: String
) : DonationContentItem()

data class DonationContentSection(
    val title: String?,
    val subtitle: HtmlText?
) : DonationContentItem()