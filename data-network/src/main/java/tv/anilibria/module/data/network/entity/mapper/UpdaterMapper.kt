package tv.anilibria.module.data.network.entity.mapper

import tv.anilibria.module.data.network.entity.app.updater.UpdateDataResponse
import tv.anilibria.module.data.network.entity.app.updater.UpdateLinkResponse
import tv.anilibria.module.domain.entity.common.asAbsoluteUrl
import tv.anilibria.module.domain.entity.updater.UpdateData
import tv.anilibria.module.domain.entity.updater.UpdateLink

fun UpdateDataResponse.toDomain() = UpdateData(
    code = code,
    build = build,
    name = name,
    date = date,
    links = links.map { it.toDomain() },
    important = important,
    added = added,
    fixed = fixed,
    changed = changed
)

fun UpdateLinkResponse.toDomain() = UpdateLink(
    name = name,
    url = url.asAbsoluteUrl(),
    type = type
)