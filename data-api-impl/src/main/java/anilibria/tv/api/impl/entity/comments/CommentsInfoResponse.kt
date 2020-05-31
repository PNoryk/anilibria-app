package anilibria.tv.api.impl.entity.comments


import com.google.gson.annotations.SerializedName

data class CommentsInfoResponse(
    @SerializedName("baseUrl") val baseUrl: String,
    @SerializedName("script") val script: String
)