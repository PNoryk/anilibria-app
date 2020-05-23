package ru.radiationx.data.entity.remote


import com.google.gson.annotations.SerializedName

data class UserResponse(
    @SerializedName("id") val id: Int,
    @SerializedName("login") val login: String,
    @SerializedName("avatar") val avatar: String?
)