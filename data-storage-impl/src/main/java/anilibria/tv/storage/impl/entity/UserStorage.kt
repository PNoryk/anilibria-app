package anilibria.tv.storage.impl.entity


import com.google.gson.annotations.SerializedName

data class UserStorage(
    @SerializedName("id") val id: Int,
    @SerializedName("login") val login: String,
    @SerializedName("avatar") val avatar: String?
)