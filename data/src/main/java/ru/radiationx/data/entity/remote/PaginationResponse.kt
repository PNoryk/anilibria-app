package ru.radiationx.data.entity.remote


import com.google.gson.annotations.SerializedName

data class PaginationResponse(
    @SerializedName("page") val page: Int,
    @SerializedName("perPage") val perPage: Int,
    @SerializedName("allPages") val allPages: Int,
    @SerializedName("allItems") val allItems: Int
)