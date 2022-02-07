package tv.anilibria.module.domain.entity.updater

import tv.anilibria.module.domain.entity.common.HtmlText

/**
 * Created by radiationx on 28.01.18.
 */
data class UpdateData(
    val code: Int,
    val build: Int,
    val name: String,
    val date: String,
    val links: List<UpdateLink>,
    val important: List<HtmlText>,
    val added: List<HtmlText>,
    val fixed: List<HtmlText>,
    val changed: List<HtmlText>,
)