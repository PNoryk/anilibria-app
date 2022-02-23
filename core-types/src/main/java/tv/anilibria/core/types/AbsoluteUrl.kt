package tv.anilibria.core.types

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class AbsoluteUrl(val value: String) : Parcelable

fun String.asAbsoluteUrl() = AbsoluteUrl(this)