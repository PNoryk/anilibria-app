package ru.radiationx.data.api.remote.release


import com.google.gson.annotations.SerializedName

data class FavoriteInfoResponse(
    @SerializedName("rating") val rating: Int,
    @SerializedName("added") val added: Boolean
)