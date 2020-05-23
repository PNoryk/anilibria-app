package ru.radiationx.data.entity.remote.common


import com.google.gson.annotations.SerializedName

data class ApiBaseResponse<T>(
    @SerializedName("status") val status: Boolean,
    @SerializedName("data") val data: T?,
    @SerializedName("error") val error: ApiErrorResponse?
)
