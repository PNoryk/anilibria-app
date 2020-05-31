package anilibria.tv.api.impl.converter

import anilibria.tv.domain.entity.checker.Update
import anilibria.tv.domain.entity.checker.UpdateLink
import anilibria.tv.api.impl.entity.checker.UpdateLinkResponse
import anilibria.tv.api.impl.entity.checker.UpdateResponse
import toothpick.InjectConstructor

@InjectConstructor
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