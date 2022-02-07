package tv.anilibria.module.domain.entity.common

data class HtmlText(val text: String)

fun String.asHtmlText() = HtmlText(this)
