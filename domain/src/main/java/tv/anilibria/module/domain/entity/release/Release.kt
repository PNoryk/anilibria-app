package tv.anilibria.module.domain.entity.release

import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.Instant
import tv.anilibria.module.domain.entity.common.AbsoluteUrl
import tv.anilibria.module.domain.entity.common.HtmlText
import tv.anilibria.module.domain.entity.common.RelativeUrl

data class ReleaseId(val id: Long)
data class ReleaseCode(val code: String)

data class Release(
    val id: ReleaseId,
    val code: ReleaseCode?,
    val names: List<HtmlText>?,
    val series: String?,
    val poster: RelativeUrl?,
    val torrentUpdate: Instant?,
    val statusName: String?,
    val status: ReleaseStatus?,
    val type: String?,
    val genres: List<String>?,
    val voices: List<String>?,
    val year: String?,
    val season: String?,
    val scheduleDay: DayOfWeek?,
    val description: HtmlText?,
    val announce: HtmlText?,
    val favoriteInfo: FavoriteInfo?,
    val showDonateDialog: Boolean?,
    val blockedInfo: BlockedInfo?,
    val webPlayerUrl: AbsoluteUrl?,
    val episodes: List<Episode>?,
    val externalPlaylists: List<ExternalPlaylist>?,
    val torrents: List<Torrent>?,
)
