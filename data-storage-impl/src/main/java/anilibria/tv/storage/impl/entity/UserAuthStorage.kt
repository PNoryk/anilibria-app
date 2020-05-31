package anilibria.tv.storage.impl.entity


import anilibria.tv.domain.entity.user.User
import com.google.gson.annotations.SerializedName

data class UserAuthStorage(
    @SerializedName("state") val state: Int,
    @SerializedName("user") val user: UserStorage?
)