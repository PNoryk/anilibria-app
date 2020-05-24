package ru.radiationx.data.api.common


import com.google.gson.annotations.SerializedName
import java.lang.RuntimeException

data class ApiErrorResponse(
    @SerializedName("code") val code: Int,
    @SerializedName("message") override val message: String?,
    @SerializedName("description") val description: String?
) : RuntimeException()