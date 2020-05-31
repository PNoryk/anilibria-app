package anilibria.tv.api.impl.entity.config


import com.google.gson.annotations.SerializedName

data class ConfigResponse(
    @SerializedName("addresses") val addresses: List<ApiAddressResponse>
)