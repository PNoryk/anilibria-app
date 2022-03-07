package tv.anilibria.feature.content.types.release

import tv.anilibria.feature.content.types.other.DataColor
import tv.anilibria.feature.content.types.other.DataIcon

data class ExternalPlaylist(
    val tag: String,
    val title: String,
    val actionText: String,
    val episodes: List<ExternalEpisode>,
    val color: DataColor,
    val icon: DataIcon
) 