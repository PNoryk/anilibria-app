package anilibria.tv.storage.impl.entity

import com.google.gson.annotations.SerializedName

data class SocialServiceStorage(
    @SerializedName("key") val key: String,
    @SerializedName("title") val title: String,
    @SerializedName("socialUrl") val socialUrl: String,
    @SerializedName("resultPattern") val resultPattern: String,
    @SerializedName("errorUrlPattern") val errorUrlPattern: String
)