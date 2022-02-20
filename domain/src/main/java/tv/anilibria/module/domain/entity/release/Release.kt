package tv.anilibria.module.domain.entity.release

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.Instant
import tv.anilibria.core.types.AbsoluteUrl
import tv.anilibria.core.types.HtmlText
import tv.anilibria.core.types.RelativeUrl
import tv.anilibria.module.domain.entity.ReleaseGenre
import tv.anilibria.module.domain.entity.ReleaseSeason
import tv.anilibria.module.domain.entity.ReleaseYear

@Parcelize
data class ReleaseId(val id: Long) : Parcelable

@Parcelize
data class ReleaseCode(val code: String) : Parcelable

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

    // todo create link
    val link: AbsoluteUrl? = TODO()

    // todo чекнуть места, где юзаются просто names
    val titleRus: HtmlText? = names?.getOrNull(0)
    val titleEng: HtmlText? = names?.getOrNull(1)
}
