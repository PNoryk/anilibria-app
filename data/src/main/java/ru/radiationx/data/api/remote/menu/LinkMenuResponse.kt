package ru.radiationx.data.api.remote.menu

import com.google.gson.annotations.SerializedName

data class LinkMenuResponse(
    @SerializedName("title") val title: String,
    @SerializedName("absoluteLink") val absoluteLink: String?,
    @SerializedName("sitePagePath") val sitePagePath: String?,
    @SerializedName("icon") val icon: String?
)