package anilibria.tv.api.impl.common

import anilibria.tv.api.common.HtmlUnescapeTool

class DummyHtmlUnescapeTool : HtmlUnescapeTool {
    override fun unescape(source: String?): String? = source
}