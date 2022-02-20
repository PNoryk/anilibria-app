package tv.anilibria.core.types

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class RelativeUrl(val url: String) : Parcelable

fun String.asRelativeUrl() = RelativeUrl(this)