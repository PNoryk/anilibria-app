package tv.anilibria.feature.page.data.remote

import tv.anilibria.core.types.asHtmlText
import tv.anilibria.module.domain.entity.page.PageLibria

fun PageLibriaResponse.toDomain() = PageLibria(
    title = title,
    content = content.asHtmlText()
)