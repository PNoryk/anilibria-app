package tv.anilibria.module.domain.entity.release

import tv.anilibria.module.domain.entity.other.DataColor
import tv.anilibria.module.domain.entity.other.DataIcon

data class ExternalPlaylist(
    val tag: String,
    val title: String,
    val actionText: String,
    val episodes: List<ExternalEpisode>,
    val color: DataColor,
    val icon: DataIcon
) 