package anilibria.tv.storage.impl.entity

import com.google.gson.annotations.SerializedName

data class LinkMenuStorage(
    @SerializedName("title") val title: String,
    @SerializedName("absoluteLink") val absoluteLink: String?,
    @SerializedName("sitePagePath") val sitePagePath: String?,
    @SerializedName("icon") val icon: String?
)