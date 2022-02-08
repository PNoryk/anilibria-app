package tv.anilibria.module.domain.entity.donation

import tv.anilibria.core.types.AbsoluteUrl
import tv.anilibria.core.types.HtmlText
import tv.anilibria.module.domain.entity.other.DataColor
import tv.anilibria.module.domain.entity.other.DataIcon

sealed class DonationContentItem

data class DonationContentButton(
    val tag: String?,
    val text: String,
    val link: AbsoluteUrl?,
    val color: DataColor?,
    val icon: DataIcon?
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