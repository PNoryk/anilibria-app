package anilibria.tv.api.impl.converter

import anilibria.tv.api.common.HtmlUnescapeTool
import anilibria.tv.api.impl.common.dateFromSec
import anilibria.tv.domain.entity.youtube.Youtube
import anilibria.tv.api.impl.entity.youtube.YouTubeResponse
import toothpick.InjectConstructor

@InjectConstructor
class YoutubeConverter(
    private val unescapeTool: HtmlUnescapeTool
) {

    fun toDomain(response: YouTubeResponse) = Youtube(
        id = response.id,
        title = response.title?.let { unescapeTool.unescape(it).toString() },
        image = response.image,
        vid = response.vid,
        views = response.views,
        comments = response.comments,
        timestamp = response.timestamp.dateFromSec()
    )
}