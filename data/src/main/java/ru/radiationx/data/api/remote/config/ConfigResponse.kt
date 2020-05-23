package ru.radiationx.data.api.remote.config


import com.google.gson.annotations.SerializedName

data class ConfigResponse(
    @SerializedName("addresses") val addresses: List<ApiAddressResponse>
)