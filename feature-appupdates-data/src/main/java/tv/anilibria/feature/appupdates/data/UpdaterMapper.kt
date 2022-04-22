package tv.anilibria.feature.appupdates.data

import tv.anilibria.core.types.asAbsoluteUrl
import tv.anilibria.core.types.asHtmlText
import tv.anilibria.feature.appupdates.data.domain.UpdateData
import tv.anilibria.feature.appupdates.data.domain.UpdateLink
import tv.anilibria.feature.appupdates.data.domain.UpdateLinkType
import tv.anilibria.feature.appupdates.data.response.UpdateDataResponse
import tv.anilibria.feature.appupdates.data.response.UpdateDataWrapperResponse
import tv.anilibria.feature.appupdates.data.response.UpdateLinkResponse

fun UpdateDataWrapperResponse.toDomain() = update.toDomain()

fun UpdateDataResponse.toDomain() = UpdateData(
    code = code,
    build = build,
    name = name,
    date = date,
    links = links.map { it.toDomain() },
    important = important.map { it.asHtmlText() },
    added = added.map { it.asHtmlText() },
    fixed = fixed.map { it.asHtmlText() },
    changed = changed.map { it.asHtmlText() }
)

fun UpdateLinkResponse.toDomain() = UpdateLink(
    name = name,
    url = url.asAbsoluteUrl(),
    type = type.toUpdateLinkType()
)

fun String.toUpdateLinkType() = when (this) {
    "file" -> UpdateLinkType.FILE
    "site" -> UpdateLinkType.SITE
    else -> UpdateLinkType.UNKNOWN
}