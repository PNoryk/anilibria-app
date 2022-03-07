package tv.anilibria.feature.domain.entity.release

import tv.anilibria.feature.domain.entity.other.DataColor
import tv.anilibria.feature.domain.entity.other.DataIcon

data class ExternalPlaylist(
    val tag: String,
    val title: String,
    val actionText: String,
    val episodes: List<ExternalEpisode>,
    val color: DataColor,
    val icon: DataIcon
) 