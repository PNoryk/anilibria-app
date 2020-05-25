package ru.radiationx.data.api.remote.pagination


import com.google.gson.annotations.SerializedName

data class PaginatedResponse<T>(
    @SerializedName("items") val items: List<T>,
    @SerializedName("pagination") val pagination: PaginationResponse
)