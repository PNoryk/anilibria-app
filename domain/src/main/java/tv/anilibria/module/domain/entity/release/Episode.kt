package tv.anilibria.module.domain.entity.release

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import tv.anilibria.core.types.AbsoluteUrl

@Parcelize
data class EpisodeId(
    val id: Long,
    val releaseId: ReleaseId
) : Parcelable

data class Episode(
    val id: EpisodeId,
    val title: String?,
    val urlSd: AbsoluteUrl?,
    val urlHd: AbsoluteUrl?,
    val urlFullHd: AbsoluteUrl?,
    val srcUrlSd: AbsoluteUrl?,
    val srcUrlHd: AbsoluteUrl?,
    val srcUrlFullHd: AbsoluteUrl?
)