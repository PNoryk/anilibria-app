package tv.anilibria.feature.domain.entity.release

import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.Instant
import tv.anilibria.core.types.AbsoluteUrl
import tv.anilibria.core.types.HtmlText
import tv.anilibria.core.types.RelativeUrl
import tv.anilibria.feature.domain.entity.ReleaseGenre
import tv.anilibria.feature.domain.entity.ReleaseSeason
import tv.anilibria.feature.domain.entity.ReleaseYear

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
    val genres: List<ReleaseGenre>?,
    val voices: List<String>?,
    val year: ReleaseYear?,
    val season: ReleaseSeason?,
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
) {

    val link: RelativeUrl? = code?.let {
        RelativeUrl("/release/${it.code}.html")
    }

    val nameRus: HtmlText? = names?.getOrNull(0)
    val nameEng: HtmlText? = names?.getOrNull(1)
}
