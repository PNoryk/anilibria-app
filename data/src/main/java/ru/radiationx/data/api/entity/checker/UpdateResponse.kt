package ru.radiationx.data.api.entity.checker


import com.google.gson.annotations.SerializedName

data class UpdateResponse(
    @SerializedName("version_code") val versionCode: String,
    @SerializedName("version_name") val versionName: String,
    @SerializedName("version_build") val versionBuild: String,
    @SerializedName("build_date") val buildDate: String,
    @SerializedName("links") val links: List<UpdateLinkResponse>,
    @SerializedName("important") val important: List<String>,
    @SerializedName("added") val added: List<String>,
    @SerializedName("fixed") val fixed: List<String>,
    @SerializedName("changed") val changed: List<String>
)