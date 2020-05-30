package ru.radiationx.data.api.entity.common


import com.google.gson.annotations.SerializedName

data class ApiErrorResponse(
    @SerializedName("code") val code: Int,
    @SerializedName("message") override val message: String?,
    @SerializedName("description") val description: String?
) : RuntimeException()