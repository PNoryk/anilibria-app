package tv.anilibria.feature.domain.entity.release

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class EpisodeId(
    val id: Long,
    val releaseId: ReleaseId
) : Parcelable