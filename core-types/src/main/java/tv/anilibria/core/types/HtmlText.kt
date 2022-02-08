package tv.anilibria.core.types

data class HtmlText(val text: String)

fun String.asHtmlText() = HtmlText(this)
