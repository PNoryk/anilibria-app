package ru.radiationx.data.entity.remote


import com.google.gson.annotations.SerializedName

data class FavoriteInfoResponse(
    @SerializedName("rating") val rating: Int,
    @SerializedName("added") val added: Boolean
)