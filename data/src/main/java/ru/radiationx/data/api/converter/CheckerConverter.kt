package ru.radiationx.data.api.converter

import ru.radiationx.data.adomain.checker.Update
import ru.radiationx.data.adomain.checker.UpdateLink
import ru.radiationx.data.api.remote.checker.UpdateLinkResponse
import ru.radiationx.data.api.remote.checker.UpdateResponse


class CheckerConverter {

    fun toDomain(response: UpdateResponse) = Update(
        versionCode = response.versionCode,
        versionName = response.versionName,
        versionBuild = response.versionBuild,
        buildDate = response.buildDate,
        links = response.links.map { toDomain(it) },
        important = response.important,
        added = response.added,
        fixed = response.fixed,
        changed = response.changed
    )

    private fun toDomain(response: UpdateLinkResponse) = UpdateLink(
        name = response.name,
        url = response.url,
        type = response.type
    )
}