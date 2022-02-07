package tv.anilibria.module.data.network.entity.mapper

import tv.anilibria.module.data.network.entity.app.updater.UpdateDataResponse
import tv.anilibria.module.data.network.entity.app.updater.UpdateLinkResponse
import tv.anilibria.module.domain.entity.common.asAbsoluteUrl
import tv.anilibria.module.domain.entity.common.asHtmlText
import tv.anilibria.module.domain.entity.updater.UpdateData
import tv.anilibria.module.domain.entity.updater.UpdateLink
import tv.anilibria.module.domain.entity.updater.UpdateLinkType

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