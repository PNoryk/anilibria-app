package anilibria.tv.api.impl.entity.checker


import com.google.gson.annotations.SerializedName

data class CheckerResponse(
    @SerializedName("update") val update: UpdateResponse
)