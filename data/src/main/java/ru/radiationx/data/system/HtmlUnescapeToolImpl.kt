package ru.radiationx.data.system

import android.text.Html
import anilibria.tv.api.common.HtmlUnescapeTool
import toothpick.InjectConstructor

@InjectConstructor
class HtmlUnescapeToolImpl: HtmlUnescapeTool {

    override fun unescape(source: String?): String? = source?.let { Html.fromHtml(it).toString() }
}