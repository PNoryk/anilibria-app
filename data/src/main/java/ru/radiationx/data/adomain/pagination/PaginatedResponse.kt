package ru.radiationx.data.adomain.pagination


import com.google.gson.annotations.SerializedName

data class PaginatedResponse<T>(
    @SerializedName("items") val items: List<T>,
    @SerializedName("pagination") val pagination: PaginationResponse
)